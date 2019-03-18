# Duplicate


programing written in JAVA 8 .

the program find duplicate image in directory by **raw data** , not by any meta data . 

# how its work ??

each image have pixels,so its array of bytes .

for each image we sample group of bytes and generate a unique hash .

same images will be get same hash so we can remove extra images.

we create a hash table of key : hash and value : counter of images in same hash ,then we remove others .

* for high performance we parallel the jobs by java 8 functions .

20 GB in 1 seconds without GUI - O/I . 

