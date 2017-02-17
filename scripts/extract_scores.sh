#!/bin/bash
#############################
# This script is used to extract image level 
# score from scores.txt file . The input to 
# this script is the root directorty and the 
# directory in which scores.txt file is kept. 
############################


function get_image_score {
    #image_score_file=image_scores.txt
    #full_path=$1
    #score_file_path=$2
    #score_file="$score_file_path/$score_file"
    #image_scores_file_path="$score_file_path/$score_file"
    #grep -E "*image*"  $score_file | cut -d' ' -f4 > $image_scores_file_path
    score_file=scores.txt
    hi_score_file=image_scores.txt
    full_path=$1
    score_file_path=$2
    echo $full_path
    echo $score_file_path
    score_file="$score_file_path/$score_file"
    echo $
    #filtered_score_path="$full_path/$file"
    hi_scores_file_path="$score_file_path/$hi_score_file"
    echo $hi_scores_file_path
    grep -E "*image*"  $score_file | cut -d' ' -f5 > $hi_scores_file_path


}

function get_hi_score {
    score_file=scores.txt
    hi_score_file=hi_scores.txt
    full_path=$1
    score_file_path=$2
    echo $full_path
    echo $score_file_path
    score_file="$score_file_path/$score_file"
    echo $
    #filtered_score_path="$full_path/$file"
    hi_scores_file_path="$score_file_path/$hi_score_file"
    echo $hi_scores_file_path
    grep -E "*heterogeneity*"  $score_file | cut -d' ' -f5 > $hi_scores_file_path

}

function get_mitochondria_score {
    score_file=scores.txt
    mitochondria_score_file=mitochondria_scores.txt
    full_path=$1
    score_file_path=$2
    echo $full_path
    echo $score_file_path
    score_file="$score_file_path/$score_file"
    echo $
    #filtered_score_path="$full_path/$file"
    mitochondria_scores_file_path="$score_file_path/$mitochondria_score_file"
    #grep -E  "*mitochondrial score*"  $score_file | cut -d' ' -f1 >> $mitochondria_scores_file_path
    grep -E  "*mitochondrial score*"  $score_file > $mitochondria_scores_file_path

}


function get_mitochondria_score_without_replicate_info {
    score_file=scores.txt
    mitochondria_score_file=raw_mitochondria_scores.txt
    full_path=$1
    score_file_path=$2
    echo $full_path
    echo $score_file_path
    score_file="$score_file_path/$score_file"
    echo $
    #filtered_score_path="$full_path/$file"
    mitochondria_scores_file_path="$score_file_path/$mitochondria_score_file"
    grep -E  "*mitochondrial score*"  $score_file | cut -d' ' -f5 > $mitochondria_scores_file_path
    #grep -E  "*mitochondrial score*"  $score_file > $mitochondria_scores_file_path

}


root_dir="$2"
score_dir=scores
#file=filtered_score.txt
echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dire : $num_dir"
for dir in $directories
do
            full_path="$root_dir/$dir"
            score_file_path="$full_path/$score_dir"
    case "$1" in 
        image) 
            get_image_score $full_path $score_file_path
            ;;
        hi) 
           get_hi_score $full_path $score_file_path 
            ;;
        rep_mit) 
            get_mitochondria_score $full_path $score_file_path
            ;;
 	mit) 
            get_mitochondria_score_without_replicate_info $full_path $score_file_path
            ;;
        *)
            echo $"Usage: $0                                                                      {image | hi | rep_mit | mit}" 
            exit 1

        esac
    done
