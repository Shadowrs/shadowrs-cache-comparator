# valkyr-cache-comparer

Copy `example.app.conf` and rename it to `app.conf`

Set the paths in app.conf making sure there are no backslashes in the paths

```
# no backslashes

into_cache = "B:/og-kronos"

from_cache = "B:/rev202/cache"

xteas_file = "C:/Desktop/keys199-2021-08-25.json"

xteas_format = "POLAR"
```

Run `CompareCaches`

Check the /logs/ folder:

![logs](https://i.imgur.com/6QWebW6.png)

Example of logs:

```
892  [main] INFO  SKELETONS - archives in SKELETONS: 2395 vs 2779 
894  [main] INFO  SKELETONS - stored 2779 archive ids 
915  [main] INFO  SKELETONS - idx SKELETONS archive 81 has new file 461 
915  [main] INFO  SKELETONS - idx SKELETONS archive 81 has new file 462 
955  [main] INFO  SKELETONS - idx SKELETONS archive 198 file 276 length changed from old 220 to new 221 by 137 bytes 
955  [main] INFO  SKELETONS - idx SKELETONS archive 198 file 279 length changed from old 239 to new 240 by 156 bytes 
1856 [main] INFO  SKELETONS - idx SKELETONS new archive: 2406 with 20 file ids 
9419 [main] INFO  MODELS - archives in MODELS: 59114 vs 59133 
9424 [main] INFO  MODELS - stored 59133 archive ids 
9440 [main] INFO  MODELS - idx MODELS archive 210 file 0 length changed from old 2310 to new 2053 by -257 bytes 
9452 [main] INFO  MODELS - idx MODELS archive 387 file 0 length changed from old 2426 to new 2432 by 6 bytes 
2092 [main] INFO  CONFIG - idx CONFIG archive 5 has new file 634 
2092 [main] INFO  CONFIG - idx CONFIG archive 5 has new file 10002 
2092 [main] INFO  CONFIG - idx CONFIG archive 5 has new file 10006 
3381 [main] INFO  CONFIG - idx CONFIG archive 6 file 1 length changed from old 58 to new 61 by 3 bytes 
3381 [main] INFO  CONFIG - idx CONFIG archive 6 file 6 length changed from old 51 to new 54 by 3 bytes 
6360 [main] INFO  CONFIG - idx CONFIG archive 16 has new file 3149 
6360 [main] INFO  CONFIG - idx CONFIG archive 19 file 379 length changed from old 1 to new 2 by 1 bytes 
6360 [main] INFO  CONFIG - idx CONFIG archive 19 has new file 381 
```

For config here is a snippet of the ID's from [OpenRS cache editor](https://github.com/kfricilone/OpenRS)

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
