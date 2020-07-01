# jfillin
 fill-in your command and execute it. Written in Pure Java 11 .
 
# How to use ? 

Just pass arguments to jfill that you want to fill.

```
jfill echo {{hello}} {{world}}
```
After this you will be promoted to fill each parameter you specified inside `{{}}`
```
  jfill echo {{hello}} {{world}}
hello: Pure
world: Java
Pure Java
```
![Example1](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/example1.png)

If you execute this command again then you could select 
value from history. To trigger history completion press **TAB** and you will
see the list of choices

## TAG
But the main power comes when you use tags. Why?
Some times you need to connect to psql but use different credentials depending
on machine you are connecting(Prod,Stage,Local).
In this case you could use tags

```
  jfill psql -U {{psql:user}} -P {{psql:port}}
```
Fill your credentials for local environment then call this command again
and type your stage server credentials.
Finally if you type this command for the third time then 
you can choose between two credentials using **TAB**.


![Example2](https://raw.githubusercontent.com/strogiyotec/jfillin/master/images/example2.png)

**jfill** respects [XDG](https://specifications.freedesktop.org/basedir-spec/basedir-spec-latest.html) standart. So your config 
with previous commands history will be stored in `~/.config/jfill/jfill.json` file

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
