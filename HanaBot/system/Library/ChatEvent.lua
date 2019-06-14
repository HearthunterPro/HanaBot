local message = eventcall.Message

local moneyread = require("MoneyReadM")
local showitem = require("ShowItem")
local chattohana = require("ChattoHana")
local chatcom = require("Chatcom")

local hanachat = require("hanachat")
local database = require("database")
local runable = require("runable")

local Name = eventcall.PlayerName

if (eventcall.Cancel) then return end

local sh = database.get("sayhana.t." .. Name)
if (sh ~= nil) then 
	if string.contains(message, ", ") then
		message = string.gsub(message, ", ", ",")
		local d = string.split(message, ",")
		for key,value in pairs(d) do
			chattohana.call(Name,message)
			sleep(2)
		end
		return
	end
	chattohana.call(Name,message)
	return
end

if (string.startswith(string.lower(message), "hana") == false) then return end

if string.startswith(string.lower(message), "hana") then
	message = string.sub(message, 6)
end

if (message == "" or message == " ") then 
	chattohana.start(Name,message)
	return
end 

if (chatcom.call(Name,message,false)) then return end
if (showitem.call(Name,message)) then return end
if (moneyread.call(Name,message)) then return end
sleep(2)
hanachat.setlevel(Name)
hanachat.send("I do not understand the question?")