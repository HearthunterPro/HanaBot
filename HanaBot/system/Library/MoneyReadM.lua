local money = require("money")
local hanachat = require("hanachat")
local m = {}

function m.money(name)
	local moneys = money.get(name)
	if moneys == nil then
		moneys = 0
	end
	return moneys
end

function m.hanac(name, num)
	local moneys = money.get(name)
	if (num == 0) then
		hanachat.send("you have money " .. moneys)
	end
end

function m.findpay(message,finding)
	local k = string.index(message, finding)
	local d = string.sub(message, k+string.len(finding)+1)
	k = string.index(d, " ")
	d = string.sub(d, 0, k)
	if (string.contains(d, ",")) then d = string.gsub(d, "," ," ") end
	return d
end

function m.call(name,message)
	if (string.contains(message, "money") == false) then return false end
	local newm = message
	local moneys = money.get(name)
	newm = string.lower(newm)
	if string.contains(newm,"what is money") then
		hanachat.setlevel(name)
		sleep(0.1)
		hanachat.send("yes yes, i think.")
		sleep(6)
		hanachat.send("Money is any item or verifiable record that is generally accepted as payment for goods and services and repayment of debts, such as taxes, in a particular country or socio-economic context.")
		sleep(3)
		hanachat.send("thank you wiki, because I take it from wiki.")
		return true
	elseif string.startswith(string.lower(newm), "how much money") then
		hanachat.setlevel(name)
		if (string.endswith(string.lower(newm), "how much money do i have") == false) then
			sleep(0.2)
			hanachat.send("I not have money but i think.")
		end
		sleep(1)
		m.hanac(name,0)
		return true
	elseif string.contains(newm,"pay") or string.contains(newm,"paid") then
		local k = nil
		if string.contains(newm,"pay") then
			k = m.findpay(newm, "pay ")
		elseif string.contains(newm,"paid") then
			k = m.findpay(newm, "paid ")
		end
		k = tonumber(k)
		sleep(1)
		if (k ~= nil) then
			hanachat.send("if you pay money " .. k .. ", you will left money " .. moneys - k)		
			return true
		end
	end
	
	return false
end


return m