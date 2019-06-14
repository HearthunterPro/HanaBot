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

function setd(name,times, t)
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
	
	local mt = string.split(message, " ")
	local sh = database.set("sayhana.t." .. name, t)
	
	message = string.replace(message, ".", "")
	message = string.replace(message, "?", "")
	
	
	
	if mt[1] == "who" then
		if mt[2] == nil then
			sleep(0.5)
			hanachat.send("Who?, me?")
			if setdata then
				setd(name, 20, 15)
			end
			return true
		elseif mt[2] == "are" or mt[2] == "am" then
			if mt[3] == "you" then
				sleep(0.5)
				hanachat.send("i am Botchat in server this.")
				return true
			elseif mt[3] == "i" then
				sleep(0.5)
				hanachat.send("you are player in server.")
				return true
			end
		end
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "hi" or mt[1] == "hello" then
		hanachat.send("Hi " .. name)
		return true
	elseif mt[1] == "i" then
		if mt[2] == nil then
			sleep(0.5)
			hanachat.send("I?")
			return true
		elseif mt[2] == "love" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("what you love?")
				if setdata then
					setd(name,20, 5)
				end
				return true
			elseif mt[3] == "you" then
				sleep(0.5)
				hanachat.send("I don't know you but thank you.")
				return true
			end
		elseif mt[2] == "am" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("what are you.")
				return true
			elseif mt[3] == "batman" then
				sleep(0.5)
				hanachat.send("You are not batman.")
				sleep(2)
				hanachat.send("I'm batman.")
				return true
			end
		end
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "you" then
		if mt[2] == nil then
			if sh == nil or sh < 5 then
				sleep(0.5)
				hanachat.send("me?")
				return true
			elseif sh == 5 then
				sleep(0.5)
				hanachat.send("I don't know you but thank you.")
				return true
			end
		end
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "what" then
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "when" then
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "why" then
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "where" then
		if mt[2] == nil then
			sleep(0.5)
			hanachat.send("you are in mc-test.net")
			return true
		elseif mt[2] == "am" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("where am?")
				return true
			elseif mt[3] == "i" then
				sleep(0.5)
				hanachat.send("you are in mc-test.net")
				return true
			end
		elseif mt[2] == "are" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("where are?")
				return true
			elseif mt[3] == "you" then
				sleep(0.5)
				hanachat.send("I am everywhere.")
				return true
			end
		end
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "how" then
		if mt[2] == nil then
			sleep(0.5)
			hanachat.send("How?")
			return true
		elseif mt[2] == "are" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("How are?")
				return true
			elseif mt[3] == "you" then
				sleep(0.5)
				hanachat.send("i'm fine.")
				return true
			end
		elseif mt[2] == "do" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("How do?")
				return true
			elseif mt[3] == "you" then
				if mt[4] == nil then
					sleep(0.5)
					hanachat.send("How?")
					return true
				elseif mt[4] == "do" then
					sleep(0.5)
					hanachat.send("how do you do.")
					return true
				end
			end
		elseif mt[2] == "to" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("what, how to?")
				return true
			elseif mt[3] == "play" then
				if mt[4] == nil then
					sleep(0.5)
					hanachat.send("what, how to?")
					return true
				elseif mt[4] == "this" then
					if mt[5] == nil then
						sleep(0.5)
						hanachat.send("You have to survive.")
						return true
					elseif mt[5] == "server" then
						sleep(0.5)
						hanachat.send("You have to survive with other player.")
						return true
					elseif mt[5] == "game" then
						sleep(0.5)
						hanachat.send("You have to survive.")
						return true
					end
				elseif mt[4] == "minecraft" then
					sleep(0.5)
					hanachat.send("You have to survive.")
					return true
				elseif mt[4] == "server" then
					sleep(0.5)
					hanachat.send("You have to survive with other player.")
					return true
				end
			end
		elseif mt[2] == "much" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("What, how much?")
				return true
			elseif mt[3] == "money" then
				if mt[4] == nil then
					sleep(0.5)
					hanachat.send("I don't have money.")
					return true
				elseif mt[4] == "do" then
					if mt[5] == nil then
						sleep(0.5)
						hanachat.send("What do you do with money.")
						return true
					elseif mt[5] == "i" then
						if mt[6] == nil then
							sleep(0.5)
							hanachat.send("What do you do with money.")
							return true
						elseif mt[6] == "have" then
							local m = moneyread.money(name)
							hanachat.send("You have money : " .. tostring(m))
							return true
						end
					elseif mt[5] == "you" then
						if mt[6] == nil then
							sleep(0.5)
							hanachat.send("What do i do with money.")
							return true
						elseif mt[6] == "have" then
							hanachat.send("I don't have money.")
							return true
						end
					elseif mt[5] == "her" or mt[5] == "he" or mt[5] == "she" then
						if mt[6] == nil then
							sleep(0.5)
							hanachat.send("What do " .. mt[5] .. " do with money.")
							return true
						elseif mt[6] == "have" then
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
						end
					end
				end
			end
		elseif mt[2] == "old" then
			if mt[3] == nil then
				sleep(0.5)
				hanachat.send("What, how old?")
				return true
			elseif mt[3] == "are" then
				if mt[4] == nil then
					sleep(0.5)
					hanachat.send("who, how old are?")
					return true
				elseif mt[4] == "you" then
					sleep(0.5)
					hanachat.send("i 18 year old.")
					return true
				end
			end
		end
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "do" then
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "if" then
		
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "yes" then
		if mt[2] == nil then
			if sh == nil then
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
		end
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	elseif mt[1] == "no" then
		if mt[2] == nil then
			if sh == nil then
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
		sleep(0.5)
		hanachat.send("I not understand.")
		return true
	end
	
	return false
end

return m