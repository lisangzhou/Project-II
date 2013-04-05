#!/bin/bash
A=0
B=20
while [ $A -le 20 ]
do
	CMD="java Network -q machine random"
	$CMD
	$A=$(($A + $B))
done
