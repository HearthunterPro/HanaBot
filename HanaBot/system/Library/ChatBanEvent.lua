local message = eventcall.Message
local name = eventcall.PlayerName
local hanachat = require("hanachat")
if (eventcall.Cancel) then return end
local ban = {"fuck","shit","stupid"}

for key,value in ipairs(ban) do
	if string.contains(message, value) then
		eventcall.Cancel = true
		hanachat.setlevel(name)
		hanachat.sendPlayer(name, "You cannot post a word that has been banned..")
	end
end
