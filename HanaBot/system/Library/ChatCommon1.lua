local hanachat = require("hanachat")
local database = require("database")
local server = require("server")
local moneyread = require("MoneyReadM")
local findplayer = require("FindPlayer")

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
	
	if message == "hi" or message == "hello" then
		hanachat.send("Hi " .. name)
		return true
	elseif message == "who" then
		sleep(0.5)
		hanachat.send("Who?, me?")
		if setdata then
			a(name, 20, 15)
		end
		return true
	elseif message == "who are you" then
		sleep(0.5)
		hanachat.send("i am Botchat in server this.")
		return true
	elseif message == "who am i" then
		sleep(0.5)
		hanachat.send("you are player in server this.")
		return true
	elseif message == "i love" then
		sleep(0.5)
		hanachat.send("what you love?")
		if setdata then
			a(name,20, 5)
		end
		return true
	elseif message == "i love you" then
		sleep(0.5)
		hanachat.send("I don't know you but thank you.")
		return true
	elseif message == "i am batman" then
		sleep(0.5)
		hanachat.send("You are not batman.")
		sleep(2)
		hanachat.send("I'm batman.")
		return true
	elseif message == "i am" then
		sleep(0.5)
		hanachat.send("what are you.")
		return true
	elseif message == "you" then
		if sh < 5 then
			sleep(0.5)
			hanachat.send("me?")
			return true
		elseif sh == 5 then
			sleep(0.5)
			hanachat.send("I don't know you but thank you.")
			return true
		end
	elseif message == "where am i" then
		sleep(0.5)
		hanachat.send("you are in mc-test.net")
		return true
	elseif message == "where are you" then
		sleep(0.5)
		hanachat.send("I am everywhere.")
		return true
	elseif message == "how are you" then
		sleep(0.5)
		hanachat.send("i'm fine.")
		return true
	elseif message == "how do you do" then
		sleep(0.5)
		hanachat.send("how do you do.")
		return true
	elseif message == "how to play" or message == "how to play server" or message == "how to play this server" then
		sleep(0.5)
		hanachat.send("You have to survive with other player.")
		return true
	elseif message == "how to player game" then
		sleep(0.5)
		hanachat.send("You have to survive.")
		return true
	elseif message == "how much" then
		sleep(0.5)
		hanachat.send("What, how much?")
		return true
	elseif message == "how much money" then
		sleep(0.5)
		hanachat.send("I don't have money.")
		return true
	elseif message == "how much money do you have" then
		sleep(0.5)
		hanachat.send("I don't have money.")
		return true
	elseif message == "how much money do she have" then
		local d = findplayer.a(name,5)
		if d == nil then
			sleep(0.5)
			hanachat.send("not have player in near you")
			return true
		else
			local m = moneyread.money(name)
			sleep(0.5)
			hanachat.send(mt[5] .. " have money : " .. tostring(m))
			return true
		end
	elseif message == "how old are you" then
		sleep(0.5)
		hanachat.send("i 18 year old.")
		return true
	elseif message == "yes" then
		print("sh: " .. tostring(sh))
		if sh < 4 then
			sleep(0.5)
			hanachat.send("what is yes.")
			return true
		elseif sh == 15 then
			sleep(0.5)
			hanachat.send("i am Botchat in server this.")
			return true
		elseif sh == 1001 then
			sleep(0.5)
			hanachat.send("ask me come.")
			return true
		end
	elseif message == "no" then
		if sh < 4 then
			sleep(0.5)
			hanachat.send("what is no.")
			return true
		elseif sh == 15 then
			sleep(0.5)
			hanachat.send("who is you mean?")
			return true
		elseif sh == 1001 then
			sleep(0.5)
			hanachat.send("ok.")
			if setdata then
				exitchat(name)
			end
			return true
		end
		
	end
	
	return false
end

return m