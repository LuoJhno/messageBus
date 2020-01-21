# Impl 项目

## Fork 项目

从 template_Impl Fork 新项目

## 修改代码

### 1 修改 gradle.properties

修改版本号、模块描述信息。

```
  version=0.0.1
  description=system impl project
```

### 2 build.gradle

添加项目中依赖的其它 api

```
  compile project(':frame')
  compile project(':systemApi')
```

### 3 src

refactor **com.zzb.business.demo** 包到 **com.zzb.business._<模块名称>_ **，，并编写相应的 controller，service 和 mapper，vmmapper
