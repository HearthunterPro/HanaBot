local message = eventcall.Message
local mutechat = require("MuteChat")
local hanachat = require("hanachat")
local runable = require("runable")

local Name = eventcall.PlayerName

if mutechat.check(Name) then
	eventcall.Cancel = true
	hanachat.setlevel(Name)
	hanachat.sendPlayer(Name, "now this you can't send chat.")
	return
end