#!/bin/bash
file='gitdiff'

if [ ! -f "$file" ]; then
    echo "新建"
    touch "$file"
else
    echo "删除"
    rm $file
    echo "再新建"
    touch "$file"
fi

current=`pwd`

echo $current
cd  ~/github/JavaLearning




echo $1

git diff $1 >> $file

mv ./$file $current/$file

echo 'finish'
