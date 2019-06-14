local message = eventcall.Message
local name = eventcall.PlayerName
local hanachat = require("hanachat")
local database = require("database")
local runable = require("runable")
local server = require("server")
local mutechat = require("MuteChat")

if (eventcall.Cancel) then return end

if (string.startswith(string.lower(message),"hana") == false) then
	return
end

local countchat = database.get("CDH.count2." .. name)
if countchat == nil then
	countchat = 0
end

if (countchat == 0) then
	local run = runable.create(0, 20)

	runable.run(run, function() 
		local times = database.get("CDH.time2." .. name)
		if times == nil then
			times = 0
		end
		times = times + 1
		if times == 30 then
			database.set("CDH.time2." .. name, nil)
			database.set("CDH.count2." .. name, nil)
			runable.stop(run)
			return
		end
		database.set("CDH.time2." .. name, times)
	end)

end

countchat = countchat + 1

local times = database.get("CDH.time2." .. name)
if (countchat >= 10 and times ~= nil) then
	eventcall.Cancel = true
	if (countchat >= 11) then
		hanachat.send("I'm warning you before." .. name)
		mutechat.set(name, 20)
		return
	end
	hanachat.setlevel(name)
	hanachat.send(name .. " fast chat 30s: 10c, I will kick you when send chat again")
end
database.set("CDH.count2." .. name, countchat)