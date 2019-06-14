local hanachat = require("hanachat")
local server = require("server")
local m = {}

function m.a(name,ru)
	local tb = server.getNear(ru,name)
	
	for key,value in pairs(tb) do
		if tostring(value) == "PLAYER" then
			return tostring(key)
		end
	end
	return nil
end

return m