local message = eventcall.Message
local name = eventcall.PlayerName
local hanachat = require("hanachat")
local database = require("database")
local runable = require("runable")

if (eventcall.Cancel) then return end

if (string.startswith(string.lower(message),"hana") == false) then
	return
end

local countchat = database.get("CDH.count." .. name)
if countchat == nil then
	countchat = 0
end

if (countchat == 0) then
	local run = runable.create(0, 10)

	runable.run(run, function() 
		local times = database.get("CDH.time." .. name)
		if times == nil then
			times = 0
		end
		
		times = times + 1
		if times == 3 then
			database.set("CDH.time." .. name, nil)
			database.set("CDH.count." .. name, nil)
			runable.stop(run)
			return
		end
		database.set("CDH.time." .. name, times)
	end)

end

countchat = countchat + 1

local times = database.get("CDH.time." .. name)
if (countchat >= 2 and times ~= nil) then
	eventcall.Cancel = true
	hanachat.setlevel(name)
	hanachat.sendPlayer(name,"delay chat to hana " .. tostring(3 - times))
end
database.set("CDH.count." .. name, countchat)