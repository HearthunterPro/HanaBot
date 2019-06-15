local hanachat = require("hanachat")

local m = {}


function m.find(msg)
	local file = io.open("logs/latest.log" , "r")
	local linetable = {}
	local l = 0
	for line in file:lines() do 
		table.insert(linetable, line)
		l = l + 1
	end
	io.close(file)
	
	local mtb = {}
	print(msg)
	for key,line in pairs(linetable) do 
		if string.contains(string.lower(line),msg) then
			if string.contains(line,"Hana") == false then
				table.insert(mtb,line)
			end
		end
		
	end
	return mtb, l
end

function m.f()
	return file
end

return m