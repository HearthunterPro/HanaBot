local hanachat = require("hanachat")
local database = require("database")
local runable = require("runable")

local m = {}

function m.check(name)
	local times = database.get("muteplayer." .. name)
	if times ~= nil then return true end
	return false
end

function m.get(name)
	local times = database.get("muteplayer." .. name)
	return times
end

function m.remove(name)
	database.set("muteplayer." .. name, nil)
	return
end

function m.set(name,t)
	local run = runable.create(0, 20)
	
	database.set("muteplayer." .. name, t)
	runable.run(run, function() 
		local times = database.get("muteplayer." .. name)
		if times == nil then
			runable.stop(run)
			return
		end
		
		if times <= 0 then
			database.set("muteplayer." .. name, nil)
			runable.stop(run)
			return
		end
		times = times - 1
		database.set("muteplayer." .. name, times)
	end)
	
	return
end

return m