#!/bin/bash
#############################
# This script is used to extract image level 
# score from scores.txt file . The input to 
# this script is the root directorty and the 
# directory in which scores.txt file is kept. 
############################
root_dir="$1"
part_dir="$2"
#file=filtered_score.txt
score_file=scores.txt
hi_score_file=hi_scores.txt
echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dire : $num_dir"
for dir in $directories
do
    full_path="$root_dir/$dir/$part_dir"
    score_file_path="$full_path/$score_file"
    echo $score_file_path
    #filtered_score_path="$full_path/$file"
    hi_scores_file_path="$full_path/$hi_score_file"
    grep "^heterogeneity"  $score_file_path | cut -d' ' -f4 >> $hi_scores_file_path
done
