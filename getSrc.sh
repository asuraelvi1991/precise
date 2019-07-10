echo $1

JavaPath='/Users/lizhen/github/JavaLearning'
file=$JavaPath$1

mkdir temp

echo $file

if [ ! -f $file ]; then
    echo "文件不存在，不进行操作"
else
    echo "文件存在，复制到当前目录下的temp目录"
    cp $file ./temp/
fi