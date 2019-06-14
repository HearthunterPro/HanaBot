local message = eventcall.Message
local name = eventcall.PlayerName
local hanachat = require("hanachat")
local database = require("database")
local runable = require("runable")

if (eventcall.Cancel) then return end

local countchat = database.get("ASC.count." .. name)
if countchat == nil then
	countchat = 0
end


if (countchat == 0) then
	local run = runable.create(0, 20)

	runable.run(run, function() 
		local times = database.get("ASC.time." .. name)
		if times == nil then
			times = 0
		end
		times = times + 1
		if times == 5 then
			database.set("ASC.time." .. name, nil)
			database.set("ASC.count." .. name, nil)
			runable.stop(run)
			return
		end
		database.set("ASC.time." .. name, times)
	end)

end

countchat = countchat + 1

local times = database.get("ASC.time." .. name)
if (countchat >= 4) then
	eventcall.Cancel = true
	hanachat.setlevel(name)
	hanachat.sendPlayer(name,"you fast chat 5 s : 4 chat. cancel ban in " .. tostring(5 - times))
end
database.set("ASC.count." .. name, countchat)