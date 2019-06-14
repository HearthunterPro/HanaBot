local system = require("systemfile")


local mytable = {
"AntiSpamChatEvent.lua",
"ChatBanEvent.lua",
"ChatCommon.lua",
"ChatDelayHana.lua",
"ChatEvent.lua",
"ChatSpamS10.lua",
"ChattoHana.lua",
"FindNearPlayer.lua",
"Findlog.lua",
"LoadLibrary.lua",
"MoneyReadM.lua",
"MuteChat.lua",
"MuteChatEvent.lua",
"ShowItem.lua",
"datafile.lua",
}


local link = "https://raw.githubusercontent.com/HearthunterPro/HanaBot/master/HanaBot/system/Library/"

system.download("plugin/Hanabot/system/ini.lua", "https://raw.githubusercontent.com/HearthunterPro/HanaBot/master/HanaBot/system/ini.lua")

for key,value in pairs(mytable) do
	system.download("plugin//Hanabot//system//Library//" ..value, link .. value)
end
