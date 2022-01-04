API基本格式：http://ip:port/索引/类型/文档ID

常用动词：GET/PUT/POST/DELETE

### **一、索引的增删改查**

#### **1、新增索引**

```json5
PUT /index_name
{
    "settings": {
        // 指定分片数量，创建后不能调整，只能重建
        // 一个 分片 是一个底层的工作单元 ，它仅保存了全部数据中的一部分。
        "number_of_shards": 5,
                
        // 指定副本数量, 副本分片的数量，副本分片就是主分片的一个拷贝
        // 主分片和他的副本分片不会存在于同一个节点，保证数据高可用性
        "number_of_replicas": 1,
        
        // 指定分词器
        "analysis": {
        	//自定义分析器示例，使用自定义的映射，把 & 替换为 and
        	"char_filter": {
        		"type": "mapping",
        		"mappings": ["&=> and"]
        	}
        	// 字符过滤器
            "filter": {
                "my_synonym_filter": {
                    "type": "synonym",
                    "synonyms_path": "analysis/synonyms.txt"
                  }
            }, 
         	 // 分析器
            "analyzer": {
                 "ik_syno": {
                      "type": "custom",
                      "char_filter"：["char_filter"]
                      "tokenizer": "ik_smart",
                      "filter": [
                      	//使用小写词过滤器处理
                        "lowercase",
                        //自定义的字符过滤器
                        "my_synonym_filter"
                    ]
                },
                "ik_syno_max": {
                    "type": "custom",
                    "tokenizer": "ik_max_word",
                    "filter": [
                      "lowercase",
                      "my_synonym_filter"
                    ]
                }
            }
        },
        "index": {
            //指定默认分词器为 ik_max_word
            "analysis.analyzer.default.type": "ik_max_word"
        }
    },
    // 映射
    "mappings": {
        // 属性
        "properties": {
            //字段名称
            "fieldName": {
                //类型
                "type": "keyword",
                //指定分词器
                "analyzer":"ik_syno_max"
            },
            "test_field": {
            	//text类型全文搜索
                "type": "text",
                //为不同的目的 以不同的方式 索引相同的字段
                "fields": {
                	//keyword支持聚合查询
                    "keyword": {
                        "type": "keyword",
                        //最大长度
                        "ignore_above": 256
                    }
                }
            }
        }
    }
}
```
- 测试分析器

  ```
  GET /index_test/_analyze
  {
    "analyzer": "ik_syno_max",
    "text": "你好，中国人"
  }
  
  ```

  

#### 2、删除索引

```
//删除指定索引
DELETE /index_name
//删除多个索引
DELETE /index_name,index_name2
DELETE /index_*
//删除全部索引
DELETE /_all
DELETE /*
//静止删除全部索引命令，配置文件中修改
action.destructive_requires_name: true
```

#### 3.更新索引

```json5
//修改索引的副本分片数
PUT /index_name/_settings
{
	"number_of_replicas":1
}
//索引中添加映射，可同时向多个索引添加映射
PUT /index_name,index_name_2/_mapping/_doc
{
	"fieldName": {
		"type": "text"
	}
}
//更新全部索引
PUT /index_name/_mapping
{
	"properties": {
		"fieldName": {
			"type": "keyword"
		},
		"fieldNameTwo": {
			"properties": {
				"fisrtName": {
					"type": "text",
		
				}
			}
		},
		"fieldNameThree": {
			"type":"long"
		}
	}
}
注：修改映射时发现，只能新增映射，不能删除，已有的字段及类型不能修改，可以修改字段最大长度
```

#### 4.查看索引

```
//查看索引
GET /index_name

//查看索引映射
GET /index_name/_mapping

//查看具体某个字段的映射 
GET /index_name/_mapping/field/field_name
注意：这个查看需要到具体字段，如上述的fieldNameTwo，它是一个对象数据类型
如果使用：GET /index_name/_mapping/field/fieldNameTwo，返回来的是空的，如图1，正确的，如图2

```

1. ![image-20210312173447815](https://gitee.com/liugguisheng/tuci/raw/master/img/20210312173447.png)

2.  ![image-20210312173551371](https://gitee.com/liugguisheng/tuci/raw/master/img/20210312173551.png)

3. 测试索引映射详情：

   ```json
   {
     "index_test" : {
       "mappings" : {
         "properties" : {
           "age" : {
             "type" : "long"
           },
           "description" : {
             "type" : "text",
             "analyzer" : "ik_syno_max"
           },
           "name" : {
             "type" : "keyword"
           },
           "name_ext" : {
             "properties" : {
               "first_name" : {
                 "type" : "text"
               },
               "last_name" : {
                 "type" : "keyword"
               }
             }
           },
           "update_test" : {
             "type" : "keyword",
             "ignore_above" : 500
           },
           "weight" : {
             "type" : "text",
             "fields" : {
               "keyword" : {
                 "type" : "keyword",
                 "ignore_above" : 256
               }
             }
           }
         }
       }
     }
   }
   ```

   

### **二、文章的增删改查**

每个文档都有与之对应的版本号vstion，更新操作会提升版本号

#### 一、新增文章

```json
//方法一：
//可以设置ID，不设置ID es将自动生成
REQUEST:
POST /index_test/_doc/id
{
  "name":"name_test",
  "age":"33",
  "description":"bad boy",
  "name_ext": {
    "first_name":"jona",
    "last_name":"hanson"
  },
  "weight":"50"
}
RESPONSE:
{
  "_index" : "index_test",
  "_type" : "_doc",
  "_id" : "gZPXJXgBCRiRCHhq54lf",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 5,
  "_primary_term" : 1
}
//方法二：
//在url末端直指定_create，如果指定了id，且已存在了该ID的文章，那么会返回错误信息
POST /index_test/_doc/gZPXJXgBCRiRCHhq54lf/_create

```

#### 二、更新文章

```json
//更新整个文档
//如果文档已存在，将会覆盖原文档，并提升一个版本，且result字段为update
REQUEST:
POST /index_test/_doc/gZPXJXgBCRiRCHhq54lf
{
  "name":"name_test",
  "age":"33",
  "description":"bad boy",
  "name_ext": {
    "first_name":"jona",
    "last_name":"hanson"
  },
  "weight":"50"
}
RESPONSE:
{
  "_index" : "index_test",
  "_type" : "_doc",
  "_id" : "gZPXJXgBCRiRCHhq54lf",
  "_version" : 2,
  "result" : "updated",
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "_seq_no" : 6,
  "_primary_term" : 1
}
//文档部分更新
REQUEST:
POST /index_test/_update/gZPXJXgBCRiRCHhq54lf
{
  "doc": {
    "weight":"32" 
  }
}
RESPONSE:
{
  "_index" : "index_test",
  "_type" : "_doc",
  "_id" : "gZPXJXgBCRiRCHhq54lf",
  "_version" : 3,
  "result" : "noop",
  "_shards" : {
    "total" : 0,
    "successful" : 0,
    "failed" : 0
  },
  "_seq_no" : 7,
  "_primary_term" : 1
}
//可设置更新失败重试次数 retry_on_conflict=重试次数
REQUEST:
POST /index_test/_update/gZPXJXgBCRiRCHhq54lf?retry_on_conflict=5
{
  "doc": {
    "weight":"32" 
  }
}

```

#### 三、文章查询

```json
       
GET /index_test/_search
{
  "query": {
    "match": {
      "weight": "321"
    }
  },
  //分页
  "from":"1",
  "size":"3"
}
//组合查询
POST /index_test/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "age": "33"
          },
          "multi_match": {
            "query": "",
            "fields": []
          },
          "range": {
            "age": {
              //大于等于
              "gte": 10,
              //大于
              "gt": 10
              //小于等于
              "lte": 20
              //小于
              "lt":20
            },
            "name": {
              "gte": 10,
              "lte": 20
            }
          },
          //精确匹配
          "terms": {
            "name": [
              "VALUE1",
              "VALUE2"
            ]
          }
        }
      ],
      "must_not": [
        {
          "match": {
            "name": "贝壳汉姆"
          }
        }
      ],
      "should": [
        {
          "match": {
            "description": "boy"
          }
        }
      ],
    }
  }
}
match 查询
无论你在任何字段上进行的是全文搜索还是精确查询，match 查询是你可用的标准查询。

multi_match 查询可以在多个字段上执行相同的 match 查询：

range 查询
range 查询找出那些落在指定区间内的数字或者时间：

term 查询
term 查询被用于精确值匹配，这些精确值可能是数字、时间、布尔或者那些 not_analyzed 的字符串：

terms 查询
terms 查询和 term 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件：

exists 查询和 missing 查询
exists 查询和 missing 查询被用于查找那些指定字段中有值 (exists) 或无值 (missing) 的文档。这与SQL中的 IS_NULL (missing) 和 NOT IS_NULL (exists) 在本质上具有共性：

must
文档 必须 匹配这些条件才能被包含进来。
must_not
文档 必须不 匹配这些条件才能被包含进来。
should
如果满足这些语句中的任意语句，将增加 _score ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。
filter
必须 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档。
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }},
            { "range": { "date": { "gte": "2014-01-01" }}}
        ]
    }
}
如果我们不想因为文档的时间而影响得分，可以用 filter 语句来重写前面的例子：
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "range": { "date": { "gte": "2014-01-01" }} 
            //range 查询已经从 should 语句中移到 filter 语句
        }
    }
}

验证查询语句，理解错误信息
为了找出 查询不合法的原因，可以将 explain 参数 加到查询字符串中
GET /gb/tweet/_validate/query?explain 
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}

按照字段的值排序
在这个案例中，通过时间来对 tweets 进行排序是有意义的，最新的 tweets 排在最前。 我们可以使用 sort 参数进行实现
GET /_search
{
    "query" : {
        "bool" : {
            "filter" : { "term" : { "user_id" : 1 }}
        }
    },
    "sort": { "date": { "order": "desc" }}
}

多级排序
假定我们想要结合使用 date 和 _score 进行查询，并且匹配的结果首先按照日期排序，然后按照相关性排序：
GET /_search
{
    "query" : {
        "bool" : {
            "must":   { "match": { "tweet": "manage text search" }},
            "filter" : { "term" : { "user_id" : 2 }}
        }
    },
    "sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
}

```

#### 四、文章删除

```json
//根据ID删除文章
DELETE /index_name/_type/id   示例：DELETE /index_test/_doc/3

```



**注意**：有时为了防止两次修改同时触发，导致前一次的修改被覆盖。文档的更新或删除 API，都可以接受 `version` 参数,可以通过指定版本号的方式来修改或删除文章：

**老版本：**为了避免修改冲突，可以在修改的时候指定版本号：例：POST /index_test/_doc/gZPXJXgBCRiRCHhq54lf?version=1

```
POST /index_test/_update/gZPXJXgBCRiRCHhq54lf?version=1
{
  "doc": {
    "weight":"32" 
  }
}
```

**新版本：** 使用if_seq_no和if_primary_term

![image-20210315110324100](https://gitee.com/liugguisheng/tuci/raw/master/img/20210315110331.png)

```
更新语句
POST /index_test/_update/id/?if_seq_no=2&if_primary_term=1
```



### 查看集群健康

```text
http://localhost:port/_cluster/health?protty

green：所有的主分片和副本分片都正常运行。
yellow：所有的主分片都正常运行，但不是所有的副本分片都正常运行。
red：有主分片没能正常运行
```




