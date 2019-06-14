local hanachat = require("hanachat")
local database = require("database")
local server = require("server")
local runable = require("runable")
local Chatcom = require("Chatcom")


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

function setd(name, times, t)
	database.set("sayhana." .. name, times)
	database.set("sayhana.t." .. name, t)
end

function m.call(name,message)
	message = string.lower(message)
	local mt = string.split(message, " ")
	local msgs = database.get("sayhana.msg." .. name)
	if msgs == message then
		hanachat.send("Why, you ask repeated?")
		return true
	end
	
	database.set("sayhana.msg." .. name, message)
	if mt[1] == "hana" then
		if mt[2] == nil then
			sleep(0.5)
			hanachat.send("what?")
			database.set("sayhana." .. name, 10)
			database.set("sayhana.t." .. name, 2)
			return true
		else
			if string.startswith(message, "hana") then
				message = string.sub(message, 6)
			end
			mt = string.split(message, " ")
		end
	end
	
	
	message = string.replace(message, ".", "")
	message = string.replace(message, "?", "")
	
	hanachat.setlevel(name)
	
	if Chatcom.call(name,message, true) then
		return true
	elseif mt[1] == "end" then
		if mt[2] == "chat" then
			sleep(0.5)
			hanachat.send("ok")
			exitchat(name)
			return true
		end
		
	elseif mt[1] == "endchat" then
		sleep(0.5)
		hanachat.send("ok")
		exitchat(name)
		return true
		
	end
	
	return false
end



function m.start(name,message)
	local fname = database.get("sayhana.now", name)
	if fname == name then
		m.call(name,message)
		return
	end
	
	if fname ~= nil then
		hanachat.setlevel(name)
		local ds = database.set("sayhana.calls." .. name)
		if ds == nil then
			ds = 0
		end
		if ds == 3 then
			server.kick(name, "I told you wait.")
			hanachat.send("I told to you " .. name .. ".")
			return
		end
		database.set("sayhana.calls." .. name, ds+1)
		sleep(0.8)
		hanachat.send("please wait " .. name .. ", I talking with " .. fname)
		return
	end
	database.set("sayhana.calls." .. name, nil)
	sleep(0.8)
	hanachat.setlevel(name)
	hanachat.send("Hi " .. name .. ", What do you want me to do?")
	local run = runable.create(0, 20)
	database.set("sayhana.now", name)
	database.set("sayhana." .. name, 20)
	database.set("sayhana.t." .. name, 1)
	database.set("sayhana.runs." .. name, run)
	
	
	runable.run(run, function()
	local sh = database.get("sayhana." .. name)
	local sd = database.get("sayhana.t." .. name)
	if sd == nil then
		sd = 0
	end
	if sh == 0 then
		if sd == 1 then
			database.set("sayhana." .. name, 10)
			database.set("sayhana.t." .. name, 2)
			hanachat.setlevel(name)
			hanachat.send("Hello " .. name .. ", can you hear me")
			return
		elseif sd == 2 then
			hanachat.setlevel(name)
			hanachat.send(":( " .. name .. " call me but don't ask ")
			exitchat(name)
			return
		elseif sd >= 3 and sd <= 1000 then
			hanachat.setlevel(name)
			hanachat.send(name .. " You have nothing to ask me, right?")
			database.set("sayhana." .. name, 12)
			database.set("sayhana.t." .. name, 1001)
			return
		elseif sd == 1001 then
			hanachat.setlevel(name)
			hanachat.send(name .. " ok")
			exitchat(name)
			return
		end
	end
	sh = sh - 1
	database.set("sayhana." .. name, sh)
	
	end)
	
	return
end

return m