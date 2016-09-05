#!/bin/bash

function count_mitochondria_in_image {
    count=0
    while IFS='' read -r line || [[ -n "$line" ]]; do
        if  [[ $line == score* ]] ;
        then
            # echo yes
            count=$((count + 1))
        else
            echo "$line : $count"
            count=0
        fi

    done < "$1"
}

root_dir="$1"
part_dir="$2"
file=filtered_score.txt
score_file=scores.txt
mit_count_file=mitochondria_count.txt
#echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dire : $num_dir"
for dir in $directories
do
    full_path="$root_dir/$dir/$part_dir"
    score_file_path="$full_path/$score_file"
    filtered_score_path="$full_path/$file"
    egrep '^File|^score' $score_file_path > $filtered_score_path
    echo "$filtered_score_path"
    mit_count_file_path="$full_path/$mit_count_file"
    count_mitochondria_in_image $filtered_score_path > $mit_count_file_path 
    total=0
    cut -d" " -f5 $mit_count_file_path | awk '{total+=$1} END {print "Total : "total}'  >> $mit_count_file_path 
    #echo "Total count : $total" >> $mit_count_file_path  
done
