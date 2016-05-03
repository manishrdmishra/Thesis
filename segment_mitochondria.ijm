var tags   = newArray("Tumor","Healthy", "Non_Mitochondria");
var colors = newArray("red","green","blue");

var commands = newArray("Select by tag...","close...");

var menu = Array.concat(commands, tags);
var pmCmds = newMenu("Popup Menu", menu);
var initial_val = 50;
var r = initial_val;
var g = initial_val;
var b = initial_val; 
var increment_by = 30;
var color = 10;
macro "Popup Menu" 
{
    cmd = getArgument();
    if (cmd=="Select by tag...")
    {
        Dialog.create("Title");
        Dialog.addChoice("select tag", tags);
        Dialog.show();
        tag = Dialog.getChoice();
        array  =selectROIS(tag);
        roiManager ('select', array);
    } 
    else if(cmd == "close...")
    {
        handleCloseCommand();

    } 
    else 
    {

        Roi.setProperty(cmd);
        color = getMatchingColor(cmd);
        Roi.setStrokeColor(color);
        Roi.setFillColor(color);
        Roi.setStrokeWidth(4);
        Roi.setName(cmd);
        roiManager('add');
    }
}

function handleCloseCommand()
{
    r = g = b = initial_val;
    dir = getDirectory("image");
    print (dir );
    print ( getTitle );
    name = dir + toString(getTitle) + '-seg';
    saveAs("Tiff",name);
    roiManager('save', dir + getTitle +'-ROIset.zip');
    roiManager('reset');
    close();

}
function fillRoiColor(cmd)
{
    if(cmd=="Tumor")
    {
        r = r + increment_by;
        setColor(r,0,0);
    }
    else if(cmd == "Healthy")
    {
        g = g + increment_by;
        setColor(0,g,0);
    }
    else if ( cmd == "Non-Mitochondria")
    {
        b = b + increment_by;
        setColor(0,0,b);
    }

    fill();
}

function selectROIS (keyword) {
	ids=newArray(0);
	for (i=0;i<roiManager('count');i++) {
		roiManager('select',i);
		if (Roi.getProperty(keyword)==true) {
			ids=Array.concat(ids,i);
		}
	}
	return ids;
}
 
function getMatchingColor(s) {
	index=-1;
	for (i=0;i<tags.length;i++) {
		if (tags[i]==s) index=i;
	}
	return colors[index];
}


