# valkyr-cache-comparer

1 Copy `example.app.conf` and rename it to `app.conf`

2 Copy `example.my-folders` and rename it to `myconfig.conf`

Set the path in app.conf which points towards myconfig.conf. This is so you can have presets of multiple comparison directories by changing only `app.conf` instead the cache paths all the time.

app.conf:

```
# no backslashes
conf_file = "my-folders.conf"
```

myconfig.conf:

```
# no backslashes
into_cache = "B:/og-kronos"

from_cache = "B:/rev202/cache"

xteas_file = "C:/Desktop/keys199-2021-08-25.json"

xteas_format = "POLAR"
```

P.S You need to run the 'jar' task on the original valkyr editor > go into `build/libs/suite-base.jar` and copy this jar into the `/lib/` folder of this project. Or download a slightly modified version of Valkyr with an adjustment to the xtea decoder this app uses [here](https://www.dropbox.com/s/6umfekzkafhzf6x/suite-base.jar?dl=1)

Run `CompareCaches`

Check the /logs/ folder:

![logs](https://i.imgur.com/6QWebW6.png)

Example of logs:

```
archives in CONFIG: 32 vs 32
stored 32 archive ids

idx CONFIG archive OBJECT has file change count 37445 vs 37434
idx CONFIG archive OBJECT file 37424 length changed from old 33 to new 63 by 30 bytes
idx CONFIG archive OBJECT has new file 50000
idx CONFIG archive OBJECT has new file 50010

idx CONFIG archive NPC has file change count 9326 vs 9299
idx CONFIG archive NPC file 9244 length changed from old 62 to new 72 by 10 bytes
idx CONFIG archive NPC has new file 15000

idx CONFIG archive SPOTANIM has new file 5033
idx CONFIG archive VARBIT has file change count 9488 vs 9489
idx CONFIG archive VARCLIENT has file change count 381 vs 382
idx CONFIG archive 28 has file change count 157 vs 159
idx CONFIG archive AREA file 525 length changed from old 49 to new 42 by -7 bytes


archives in MODELS: 38910 vs 59114
stored 59114 archive ids
idx MODELS archive 30539 file 0 length changed from old 1101 to new 1172 by 71 bytes
idx MODELS archive 37053 file 0 length changed from old 1916 to new 1867 by -49 bytes
idx MODELS archive 38909 file 0 length changed from old 12324 to new 1 by -12323 bytes
idx MODELS new archive: 38910 with empty file data [0]

idx CLIENT_SCRIPT new archive: 10183 with 1 file length 179
idx CLIENT_SCRIPT archive 2066 file 0 length changed from old 242 to new 189 by -53 bytes
```

For config here is a snippet of the ID's from [OpenRS cache editor](https://github.com/kfricilone/OpenRS)

[r-s post more info on cache](http://web.archive.org/web/20150426093916/http:/www.rune-server.org/runescape-development/rs-503-client-server/informative-threads/568002-known-cache-information-index-contents-unhashed-filenames.html)

This is also available in RuneLite/cache module

```
public enum ConfigArchive {

	AREA(35),
	ENUM(8),
	HITBAR(33),
	HITMARK(32),
	IDENTKIT(3), 
	ITEM(10), 
	INV(5), 
	NPC(9), 
	OBJECT(6), 
	OVERLAY(4), 
	PARAMS(11),
	SEQUENCE(12), 
	SPOTANIM(13),
	STRUCT(34),
	UNDERLAY(1), 
	VARBIT(14), 
	VARCLIENT(19), 
	VARCLIENTSTRING(15), 
	VARPLAYER(16);
}
  ```
.
