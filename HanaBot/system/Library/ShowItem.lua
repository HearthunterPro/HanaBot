local hanachat = require("hanachat")
local database = require("database")
local server = require("server")
local m = {}

function a(name,slot)
	local tb = database.getInfo(name)
	
	if slot == 1 then
		sleep(0.5)
		b(name,tb.ItemInHand)
	elseif slot == 2 then
		sleep(1)
		b(name,tb.ItemInHelmet)
	elseif slot == 3 then
		sleep(1)
		b(name,tb.ItemInChestplate)
	elseif slot == 4 then
		sleep(1)
		b(name,tb.ItemInLeggings)
	elseif slot == 5 then
		sleep(1)
		b(name,tb.ItemInBoots)
	end
end

function b(name,items)
	hanachat.setlevel(name)
	if items.Type == "AIR" then
		hanachat.send("your item in slot that is AIR")
		return
	end
	
	hanachat.send("your item : ")
	local meta = items.Meta
	local msg = "{\"text\":\"&a[&cItem&a]\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\""
	msg = msg .. "type: " .. items.Type .. "\n"
	msg = msg .. "Amount: " .. items.Amount .. ""
	if #meta > 0 then
		msg = msg .. "\nDisplay: " .. meta.DisplayName .. "\n"
		msg = msg .. "Lore: " .. meta.Lore .. "\n"
		msg = msg .. "Flag: " .. meta.Flag .. "\n"
		msg = msg .. "Enchant: " .. meta.Enchant .. ""
	end
	msg = msg .. "\"}}"
	for key,value in pairs(server.getOnlinePlayer()) do
		server.tellraw(value, msg)
	end
end

function m.show(name,slot)
	if (slot == "hand") then
		a(name, 1)
		return true
	elseif (slot == "head" or slot == "helmet") then
		a(name, 2)
		return true
	elseif (slot == "chestplate") then
		a(name, 3)
		return true
	elseif (slot == "legging") then
		a(name, 4)
		return true
	elseif (slot == "boot" or slot == "boots") then
		a(name, 5)
		return true
	end
	return false
end


function m.call(name,message)
	local tbm = string.split(message, " ")
	if tbm[1] == "show" and tbm[2] == "item" then
		local slot = nil
		if (tbm[3] == "in" or tbm[3] == "on") then
			if tbm[4] == "my" then
				slot = tbm[5]
			elseif tbm[4] == "you" then
				hanachat.setlevel(name)
				hanachat.send("I not have item in " .. tbm[5])
				return true
			elseif tbm[4] == nil then
				slot = tbm[4]
			end
		elseif tbm[3] == nil then
			slot = tbm[3]
		end
		
		if (slot == "hand") then
			a(name, 1)
			return true
		elseif (slot == "head" or slot == "helmet") then
			a(name, 2)
			return true
		elseif (slot == "chestplate") then
			a(name, 3)
			return true
		elseif (slot == "legging") then
			a(name, 4)
			return true
		elseif (slot == "boot" or slot == "boots") then
			a(name, 5)
			return true
		end
		sleep(0.2)
		hanachat.setlevel(name)
		hanachat.send("i not understand this slot.")
		return true
	end
	
	return false
end

return m