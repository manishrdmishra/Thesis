###################################
# Script description 
# 
# input - command name
# input - root directory
# input - name of new directory 
###################################



###################################
# Function definitions
###################################

function create_directory {
    mkdir $1
}


command_name="$1"
root_dir="$2"
#file=filtered_score.txt
#score_file=scores.txt
#mit_count_file=mitochondria_count.txt
#echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dire : $num_dir"
for dir in $directories
do
    echo $dir
    if [ "$command_name" = "create" ];
    then
        # loop over all the arguments and create 
        # a directory for them. 
        for new_dir_name in ${@:3}
        do
            echo $new_dir_name
            full_path="$root_dir/$dir/$new_dir_name"
            echo $full_path
            create_directory $full_path
        done
    fi
    #   score_file_path="$full_path/$score_file"
    #   filtered_score_path="$full_path/$file"
    #   egrep '^File|^score' $score_file_path > $filtered_score_path
    #   echo "$filtered_score_path"
    #   mit_count_file_path="$full_path/$mit_count_file"
    #   count_mitochondria_in_image $filtered_score_path > $mit_count_file_path 
    #   total=0
    #   cut -d" " -f5 $mit_count_file_path | awk '{total+=(} END {print "Total : "total}'  >> $mit_count_file_path 
    #echo "Total count : $total" >> $mit_count_file_path  
done

