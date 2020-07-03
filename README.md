# jfillin
 fill-in your command and execute it. Written in Pure Java 11 .

# About
jfill stores the history of your commands in order to give you nice autocompletion\
In order to trigger completion press TAB

# Install
In order to install it download the latest [release](https://github.com/strogiyotec/jfillin/releases/tag/1).
It contains single executable. You don't need Java in your computer to run it.
If you want to execute it from any directory then put executable into `/usr/local/bin` (You need a root access)
 
# How to use ? 

Just pass arguments to jfill that you want to fill.

```
jfill echo {{message}} 
```

Here is the gif\
![Gif1](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/notTag.gif) \
As you can see you can choose from the history pressing TAB.\
When you finish jfill will execute the command for you

## TAG
But the main power comes when you use tags. Why?
Some times you need to connect to psql but use different credentials depending
on machine you are connecting(Prod,Stage).
In this case you could use tags

```
  jfill psql -U {{psql:user}} -P {{psql:host}}
```

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
        1.0
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
It will create jfill executable in target/ folder . The size of this executable is 16MB(it's too big See [This issue](https://github.com/oracle/graal/issues/287))
In order to decrease it you can use [upx](https://github.com/upx/upx) it will decrease the size to 4 MB.

