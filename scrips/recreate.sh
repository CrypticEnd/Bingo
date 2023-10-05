#!/bin/bash
backendfile="bingo-back-end.tar"
frontendfile="bingo-front-end.tar"

function checkFiles(){
    #Docker compose
    if [ ! -f "./docker-compose.yml" ]; then
        echo "Docker compose file not found"
        exit 22
    fi
    
    #Backend
    if [ ! -f $backendfile ]; then
        echo "Backend file not found"
        exit 22
    fi
    
    #Front end
    if [ ! -f $frontendfile ]; then
        echo "frontend file not found"
        exit 22
    fi
    
}

function drop(){
    echo "removing docker compose"
    sudo docker-compose down
    
    echo "removing images"
    
    sudo docker image rm crypticend/bingo-back-end:latest
    sudo docker image rm crypticend/bingo-front-end:latest
    
    echo "drop complete"
}

function create(){
    echo "Loading images"
    
    sudo docker load -i $backendfile
    sudo docker load -i $frontendfile
    
    echo "Docker compose"
    
    sudo docker-compose up -d
    
    echo "load complete"
}

function recreate(){
    checkFiles
    drop
    create
}

# Getting file options
while getopts :fb opt
do
    case $opt in
        f)  frontendfile=${OPTARG} ;;
        b)  backendfile=${OPTARG} ;;
        \?) >&2 echo "$0: Invaild option --'$OPTARG'"
            >&2 echo "Userage: -f [file path] -b [file path]"
        exit 22;;
    esac
done

# confirm with user
echo "Do you know what your doing? This uses SUDO. REAL BAD"
read -p "(y/n): " userInput
userInput=$(echo ${userInput} | cut -c1 | tr [:lower:] [:upper:])

if [ $userInput == "Y" ] ; then
    recreate
else
    exit 22
fi

