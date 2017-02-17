#!/bin/bash
####################################################
# There are three arguments for this function 
# 1 - score_file_dir ( root directory where score file is ketp )
# 2 - text_file_path ( where information of replicates are written)
# 3 - result_file_dir ( where the resulted text file will be saved )
####################################################
#sample run command :./extract_replicate_scores.sh  /home/manish/git/Documents/fifth_sem/thesis/restult_folder/test
#egrep -n  '*L5612*|*56-12*'  scores.txt
#sed '8229q;d' scores.txt
function get_replicate_wise_scores {
    search_file_name="mitochondria_scores.txt"
    search_file_path="$1/$search_file_name"
    echo "$search_file_path"

#    increment_value=' ';
#    if [ "$2" == "hi" ];
#    then
#        increment_value=2;
#    elif [ "$2" = "image" ];
#    then
#        increment_value=1;
#    fi
#    echo "$increment_value"
#
#    #some constant values used during search
#    q="q"
#    d="d"
#    comma=";"

    # loop over the text file which contains the
    # biological replicate name 
    while IFS='' read -r line || [[ -n "$line" ]]; 
    do
        echo "Text read from file: $line"
        arr=($line)
        tLen=${#arr[@]}
        search_string='';
        for (( i=0; i<${tLen}; i++ ));
        do
            echo ${arr[$i]}
            search_string="$search_string*${arr[$i]}*|";
            echo "$search_string"
        done

        search_string="${search_string::-1}"
        echo replicate search string : "$search_string"
        result_file_name=${arr[0]}
        echo "$result_file_path"
        #lines=$(egrep -n "$search_string" "$search_file_path" | cut -d' ' -f7)
        #echo "$lines"
        #  arr=($lines)
        #    tLen=${#arr[@]}
        text_extenstion=".txt"
        replicate_score_file=replicate_scores
        result_file_path="$3/$replicate_score_file/$result_file_name$text_extension"
        grep -E "$search_string" "$search_file_path" | cut -d' ' -f5 > $result_file_path
        #echo total number of line : ${#arr[@]}
        #        for (( i=0; i<${tLen}; i++ ));
        #        do
        #            #    echo ${arr[$i]}
        #            next_line="$((${arr[$i]} + $increment_value))"
        #            #echo "$next_line" 
        #            argument="$next_line$q$comma$d"
        #            #    echo "$argument"
        #            #sed $argument $search_file_path | grep "score" >> $result_file_path
        #            sed $argument $search_file_path | grep "score" | cut -d' ' -f4 >> $result_file_path
        #        done
    done < "$2"
}
root_dir="$1"
#part_dir="$2"
cancer_dir=cancer_scores
hi_dir=hi_scores
score_dir=scores
replicate_dir=replicates
replicate_file_name=replicates.txt
score_file_name=scores.txt
#mit_count_file=mitochondria_count.txt
#echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dire : $num_dir"
for dir in $directories
do
    full_path="$root_dir/$dir"
    score_file_dir="$full_path/$score_dir"
    replicate_file_path="$full_path/$replicate_dir/$replicate_file_name"
   # options="hi"
    result_dir_path="$full_path/$replicate_dir"
    get_replicate_wise_scores $score_file_dir $replicate_file_path $result_dir_path
   # options="image"
   # result_dir_path="$full_path/$replicate_dir/$cancer_dir"
   # get_replicate_wise_scores $score_file_dir $options $replicate_file_path $result_dir_path
    #egrep '^File|^score' $score_file_path > $filtered_score_path
    #echo "$filtered_score_path"
    #mit_count_file_path="$full_path/$mit_count_file"
    #count_mitochondria_in_image $filtered_score_path > $mit_count_file_path 
    #total=0
    #cut -d" " -f5 $mit_count_file_path | awk '{total+=(} END {print "Total : "total}'  >> $mit_count_file_path 
    #echo "Total count : $total" >> $mit_count_file_path  
done 
