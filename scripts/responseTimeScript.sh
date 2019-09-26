#!/bin/bash
PREV_TOTAL=0
PREV_IDLE=0
TOTAL=100
DIFF_USAGE=100
cont=0
while [ "$DIFF_USAGE" -ge 30 -o "$cont" -le 2 ]; do
    CPU=$(adb shell $(echo sed -n 1p /proc/stat))
    IDLE=$(echo $CPU | awk '{ print $5 }') 
    IFS=' '
    line=($CPU)
    TOTAL=0
    for i in "${line[@]}"; do 
        if [ "$i" != "cpu" ]; then
            let "TOTAL=$TOTAL+$i"
        fi
    done
    
    let "DIFF_IDLE=$IDLE-$PREV_IDLE"
    let "DIFF_TOTAL=$TOTAL-$PREV_TOTAL"
    let "DIFF_USAGE=100-((100*$DIFF_IDLE)/$DIFF_TOTAL)"
    echo -en "\rCPU: $DIFF_USAGE%  \b\b"
        
    PREV_TOTAL="$TOTAL"
    PREV_IDLE="$IDLE"
    let "cont=cont+1"
    
    sleep 0.001
done
let "cont=cont-2"
exit "$cont"
