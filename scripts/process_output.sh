#!/bin/bash
##############################################
#
#
############################################
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


##############################
# This function creates a text file in
# each results-i directory. This text file 
# contains the name of all the image 
# present in that directory.
##############################
function create_name_file {
    name_file=names
    name_file_path="$2/$name_file"
    find "$2"  -name "$1" | sort > $name_file_path
}

#################################
# input - roiFolderPath 
################################

function unzip_roi_folders {
    root_dir="$1"
    directories="$(ls $root_dir)"
    echo "num of roi folder : $num_dir"
    for dir in $directories
    do
        roi_zipped_folder_name="$root_dir/$dir"
        name=$(echo $dir | cut -f 1 -d '.')
        roi_folder_name="$root_dir/$name"
        unzip -o "$roi_zipped_folder_name" -d "$roi_folder_name"
        rm "$roi_zipped_folder_name"
    done

}

#################################
# input - path of the directory 
# input - path of the java-util
################################
function extract_roi {

    roi_folder=roiFolder
    result_dir1=results-1
    name_file=names
    java_util_exe="build/install/java-util/bin/java-util"
    extract_roi_command=extractRoi
    names_file_path="$1/$result_dir1/$name_file"
    roi_folder_path="$1/$roi_folder"
    java_exe_path="$2/$java_util_exe"
    command="$java_exe_path  $extract_roi_command $names_file_path $roi_folder_path"
    eval $command
    #unzip_roi_folders  $roi_folder_path
    #($cammand "$names_file_path" "$roi_folder_path")

}
function project_scores {
    score_file=scores.txt
    score_folder=scores
    roi_folder=roiFolder
    result_dir2=results-2
    result_dir3=results-3
    projected_scores=projected_scores
    name_file=names
    java_util_exe="build/install/java-util/bin/java-util"
    project_scores_command=projectScores
    names2_file_path="$1/$result_dir2/$name_file"
    names3_file_path="$1/$result_dir3/$name_file"
    rois_file_name="$1/$roi_folder/$name_file"
    healthy_maps_file_name="$1/$result_dir2/$name_file"
    tumor_maps_file_name="$1/$result_dir3/$name_file"
    raw_png_file_name="$2/$name_file"
    java_exe_path="$3/$java_util_exe"
    score_file_path="$1/$score_folder/$score_file"
    projected_scores_path="$1/$projected_scores"
    command="$java_exe_path  $project_scores_command $rois_file_name $healthy_maps_file_name $tumor_maps_file_name $raw_png_file_name"
    eval $command > $score_file_path
    file_names=*.png*
    mv  "`pwd`"/*.png "$projected_scores_path"

}
# ./build/install/java-util/bin/java-util extractRoi /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/human_seg_png/results-1/names  /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/human_seg_png/roiFolder
# ./build/install/java-util/bin/java-util projectScores /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/human_seg_png/roiFolder/names  /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/human_seg_png/results-2/names /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_predicted_images_processed/human_seg_png/results-3/names
# /home/manish/git/Documents/fifth_sem/thesis/restult_folder/all_images/human_png/names
command="$1"
root_dir="$2"
raw_root_dir="$3"
java_util_root_dir="$4"

file=filtered_score.txt
score_file=scores.txt
result_file1=results-1
result_file2=results-2
result_file3=results-3
roi_folder=roiFolder
mit_count_file=mitochondria_count.txt
#echo "$root_dir"
num_dir="$(ls $root_dir | wc -l)"
directories="$(ls $root_dir)"
echo "num of dir : $num_dir"
for dir in $directories
do
    case "$1"  in 
        extract_roi)
            #create name files in each result folder
            # As java-util will read image files from this text file
            results1_path="$root_dir/$dir/$result_file1"
            create_name_file "*.png" $results1_path
            # extract roi
            full_path="$root_dir/$dir"
            extract_roi $full_path $java_util_root_dir
            ;;
        project_scores)
            results2_path="$root_dir/$dir/$result_file2"
            create_name_file "*.tif" $results2_path
            results3_path="$root_dir/$dir/$result_file3"
            create_name_file "*.tif" $results3_path
            roi_path="$root_dir/$dir/$roi_folder"
            create_name_file "*png*" $roi_path
            full_path="$root_dir/$dir"
            raw_full_path="$raw_root_dir/$dir"

            project_scores $full_path $raw_full_path $java_util_root_dir
            ;;
        *)
            echo $"Usage: $0 {extract_roi|project_scores|count_mitochondria|get_image_scores|get_hi_scores|get_replicate_wise_score}"
            exit 1
    esac

    #project each mitochondrial score on the images
    # full_path="$root_dir/$dir/$part_dir"
    # score_file_path="$full_path/$score_file"
    # filtered_score_path="$full_path/$file"
    # egrep '^File|^score' $score_file_path > $filtered_score_path
    # echo "$filtered_score_path"
    # mit_count_file_path="$full_path/$mit_count_file"
    # count_mitochondria_in_image $filtered_score_path > $mit_count_file_path 
    # total=0
    # cut -d" " -f5 $mit_count_file_path | awk '{total+=$1} END {print "Total : "total}'  >> $mit_count_file_path 
    # $#echo "Total count : $total" >> $mit_count_file_path  
done
