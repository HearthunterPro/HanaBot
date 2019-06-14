local file = io.open("logs/latest.log" , "r")
local hanachat = require("hanachat")

local m = {}


function m.find(msg)
	local l = 0
	local s = 0
	local mtb = {}
	for line in file:lines() do 
		if string.contains(line,msg) then
			if string.contains(line,"Hana") == false then
				table.insert(mtb,line)
				s = s + 1
			end
		end
		l = l + 1
	end
	return mtb, l, s
end

function m.f()
	return file
end

return m