# TempDeop
A minecraft plugin to temporarily op/deop with a configurable expiration time

**Commands:**
```
/tmpdeop <player(optional)>
/reop
/tmpop <player> <duration(in seconds)>
/tmpdeopadmin list 
/tmpdeopadmin remove <player>
```

**Permissions:**\
tmpdeop.admin - to use /tmpdeopadmin

**Config.yml:**
```
# Time in seconds after which /reop cannot be used(set this to 0 to disable)
expiration-time: 3600
# Commands disabled for temp op players
blockedCommands:
  - op
  - deop
  - tmpop
  - tmpdeopadmin

```
