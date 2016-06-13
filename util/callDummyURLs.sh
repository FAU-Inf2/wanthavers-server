#h=localhost:8080/v1
lh=faui21f.informatik.uni-erlangen.de:9090/v1
media=media
usr=users
des=desires
rat=ratings
dm=dummy

curl $lh/$media/$dm
curl $lh/$usr/$dm
curl $lh/$des/$dm
curl $lh/$usr/1/$rat/$dm
curl $lh/$usr/2/$rat/$dm
curl $lh/$des/0/havers/$dm
curl $lh/$des/0/flags/$dm
