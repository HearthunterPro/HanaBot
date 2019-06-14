local m = {}

function findstr(str)
	local l = string.index(str, "\"")
	local l2 = string.indexl(str, "\"")
	local d = str:sub(l2+1)
	local m = string.index(d,",")
	local k = d:sub(m+1)
	local m2 = string.index(k,",")
	return str:sub(l+1,l2-1), tonumber(d:sub(m+1,m2)), tonumber(k:sub(m2+1))
end

function findstr2(str)
	local l = string.index(str, "\"")
	local l2 = string.indexl(str, "\"")
	local d = str:sub(l2+1)
	local m = string.index(d,",")
	local sd = d:sub(m+1)
	local tsd = string.split(sd , ",")
	return str:sub(l+1,l2-1), tonumber(tsd[1]), tonumber(tsd[2]), tsd[3]
end

function m.linefd(msg)
	local tb = string.split(msg, ">")
	local h = tb[1]
	local e = tb[2]
	return h , e
end


function m.search(msg,sh)
	local file = io.open("plugins/HanaBot/data/datachat.txt" , "r")
	for line in file:lines() do 
		print(line)
		local h,e = m.linefd(line)
		local h2,h3,h4 = findstr(h)
		
		h4 = tonumber(h4)
		
		local reading = false
		
		if msg == h2 then
			if h3 == -1 or h3 == 0 then
				reading = true
			end
			if h4 <= 0 then
				if h3 == sh then
					reading = true
				end
			elseif h4 == 1 then
				if h3 >= sh then
					reading = true
				end
			elseif h4 == 2 then
				if h3 <= sh then
					reading = true
				end
			end
		end
		
		if reading then
			return true , findstr2(e)
		end
		
		
	end
	return false
end

return m
