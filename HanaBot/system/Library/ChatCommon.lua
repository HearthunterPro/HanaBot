local hanachat = require("hanachat")
local database = require("database")
local server = require("server")
local moneyread = require("MoneyReadM")
local findplayer = require("FindPlayer")
local findlog = require("findlog")
local datafile = require("datafile")

local m = {}

function exitchat(name)
	database.set("sayhana.now", nil)
	database.set("sayhana." .. name, nil)
	database.set("sayhana.t." .. name, nil)
	database.set("sayhana.msg." .. name, nil)
	local run = database.get("sayhana.runs." .. name)
	
	runable.stop(run)
	database.set("sayhana.runs." .. name, nil)
end

function a(name,times, t)
	database.set("sayhana." .. name, times)
	database.set("sayhana.t." .. name, t)
end

function editshort(message)
	message = string.replace(message,"'m", " am")
	message = string.replace(message,"'s", " is")
	message = string.replace(message,"'re", " are")
	message = string.replace(message,"'ll", " will")
	message = string.replace(message,"'ve", " have")
	return message
end


function m.call(name,message,setdata)
	message = string.lower(message)
	message = editshort(message)
	local sh = database.get("sayhana.t." .. name)
	if sh == nil then
		sh = 0
	end
	message = string.replace(message, ".", "")
	message = string.replace(message, "?", "")
	local x1,x2,x3,x4,x5 = datafile.search(message, sh)
	if x1 then
		if setdata then
			if tonumber(x4) ~= 0 then
				a(name, 20, x4)
			end
		end
		
		x2 = string.replace(x2, "%player%" , name)
		sleep(x3)
		if x2 ~= nil and tostring(x2) ~= "nil" then
			hanachat.send(tostring(x2))
		end
		
		if x5 ~= nil then
			if x5 == "exitchat" then
				exitchat(name)
			elseif x5 == "shehavemoney" then
				local d = findplayer.a(name,5)
				if d == nil then
					sleep(0.5)
					hanachat.send("not have player in near you")
					return true
				else
					local m = moneyread.money(name)
					sleep(0.5)
					hanachat.send("she have money : " .. tostring(m))
					return true
				end
				
			elseif x5 == "findlog" then
				local as = string.replace(message, "find log ", "")
				local mytable,lined,fline = findlog.find(message)
				database.set("findlog", mytable)
				
				sleep(0.5)
				hanachat.send("I can found " .. fline .. " line of " .. lined)
				sleep(1)
				hanachat.send("Do you want me to show all?")
			elseif x5 == "findlog2" then
				local as = "all"
				if string.contains(message, "show ") then
					as = string.replace(message, "show ", "")
				end
				
				database.set("findlog.show", as)
				sleep(0.5)
				hanachat.send("Do you want me send to private your chat?")
			elseif x5 == "findlog3" then
				local mytable = database.get("findlog")
				local as = database.get("findlog.show")
				local private = false
				if message == "yes" then
					private = true
				end
				for key,value in pairs(mytable) do
					if as == "all" then
						if private then
							hanachat.sendPlayer(name, value)
						else
							hanachat.send(value)
						end
					else
						if tonumber(key) == tonumber(as) then
							if private then
								hanachat.sendPlayer(name, value)
							else
								hanachat.send(value)
							end
						end
					end
				end
				
			end
		end
		
		return true
	end
	
	return false
end

return m