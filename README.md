# jfillin
 ![jfill build CI](https://github.com/strogiyotec/jfillin/workflows/jfill%20build%20CI/badge.svg?branch=master)
 [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://raw.githubusercontent.com/strogiyotec/jfillin/master/LICENSE)
 [![Coverage Status](https://coveralls.io/repos/github/strogiyotec/jfillin/badge.svg)](https://coveralls.io/github/strogiyotec/jfillin)
 
 Fill-in your command and execute it. Written in Pure Java 11 .

# About
jfill stores the history of your commands in order to give you nice autocompletion\
In order to trigger completion press TAB

# Install
In order to install it download the latest [release](https://github.com/strogiyotec/jfillin/releases/tag/1).
It contains single executable. You don't need Java in your computer to run it.
If you want to execute it from any directory then put executable into `/usr/local/bin` (You need a root access)

### Glibc
In order to run it you have to have **glibc** version >=2.15.
In order to check your current version run this command `ldd --version`

# How to use ? 

Just pass arguments to jfill that you want to fill.

```
jfill echo {{message}} 
```

Here is the gif\
![Gif1](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/notTag.gif) \
As you can see you can choose from the history pressing TAB.\
When you finish jfill will execute the command for you

### TAG
But the main power comes when you use tags. Why?
Some times you need to connect to psql but use different credentials depending
on machine you are connecting(Prod,Stage).
In this case you could use tags

```
  jfill psql -U {{psql:user}} -P {{psql:host}}
```

### Test API
Let's say you have an endpoint `/users` that gives you a json of users and you have two servers.
In local environment this url will look like this `localhost:8080/users` while
in production server it will look like this `www.prod.com/users`.In this case you can
use tag to group both urls (let's say tag name is **api**)
Here is an example

![Curl](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/curl.gif)


![Gif1](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/tag.gif)

As you can see jfill allows you to choose between two configurations (for local and stage environments)

## Config

**jfill** respects [XDG](https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html) standart. So your config 
with previous commands will be stored in `~/.config/jfill/jfill.json` file

## Help and version
If you execute jfill without params you will see help instruction

```
❯ ./jfill 
NAME:
        jfillin- fill your command and execute
VERSION:
        2.0
AUTHOR:
        almas337519@gmail.com

```

To check version type

```
❯ ./jfill -v
jfillin 1.0
```

## Build
In order to build it you have to install graalvm [native-image](https://www.graalvm.org/docs/reference-manual/native-image/) and Java 11 or above.
To build it run 
```
mvn clean package
```
It will create jfill executable in **target** folder . The size of this executable is 16MB(it's too big See [This issue](https://github.com/oracle/graal/issues/287))
In order to decrease it you can use [upx](https://github.com/upx/upx) it will decrease the size to 4 MB.

If you don't have a native-image in your machine then you can build a jar file using **maven profiles**
```
mvn package -P Jar
```

## JAR vs Native-Image
Here is the `time` output to see how native-image outperforms jar for cli apps

**Jar**
```
time java -jar jfill.jar -v
0.80s user 0.10s system 251% cpu 0.356 total
```

**Native-image**
```
time jfill -v
 0.02s user 0.01s system 104% cpu 0.030 total
```

# TODO
1. ~~Add async tests~~
2. ~~Remove mockito~~
3. Add encryption
