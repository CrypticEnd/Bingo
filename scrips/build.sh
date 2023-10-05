#! /bin/bash
rebuildSaves=false
rebuildDocker=false

SCRIPT_PATH="$(dirname "$0")"
COMPOSE_PATH="$SCRIPT_PATH"/../

function backend(){
    echo "----Backend----"
    
    if [ $rebuildDocker == true ] ; then
        echo "Rebuilding Docker images"

        (cd "$COMPOSE_PATH" && docker-compose rm -vsf backend)
        docker image rm crypticend/bingo-back-end:latest
        
        # Converts maven file into linx readable
        # Need to have dos2unix in path
        (cd "$COMPOSE_PATH/bingo-back-end" && dos2unix mvnw)
        
        (cd "$COMPOSE_PATH" && docker-compose up -d --build backend)
    fi
    
    if [ $rebuildSaves == true ] ; then
        echo "Rebuilding Save files"
        (cd "$SCRIPT_PATH" && docker save crypticend/bingo-back-end:latest -o bingo-back-end.tar)
    fi
    
    echo "Complete"
}

function frontend(){
    echo "----Frontend----"
    
    if [ $rebuildDocker == true ] ; then
        echo "Rebuilding Docker images"

        (cd "$COMPOSE_PATH" && docker-compose rm -vsf frontend)
        docker image rm crypticend/bingo-front-end:latest
        (cd "$COMPOSE_PATH" && docker-compose up -d --build frontend)
    fi
    
    if [ $rebuildSaves == true ] ; then
        echo "Rebuilding Save files"
        (cd "$SCRIPT_PATH" && docker save crypticend/bingo-front-end:latest -o bingo-front-end.tar)
    fi
    
    echo "Complete"
}

while getopts :bs opt
do
    case $opt in
        s)  rebuildSaves=true;;
        b)  rebuildDocker=true ;;
        \?) >&2 echo "$0: Invaild option --'$OPTARG'"
            >&2 echo "Userage: -b -s"
        exit 22;;
    esac
done

shift $(($OPTIND -1))

case "$1" in
    backend)
        backend
    ;;
    frontend)
        frontend
    ;;
    all)
        backend
        frontend
    ;;
    *)
        echo "Usage: ./build.sh [SERVICE]"
        echo "{Action} Docker images for a specific service or all services."
        echo ""
        echo "SERVICE:"
        echo "    backend      {Action} the backend Docker image."
        echo "    frontend     {Action} the frontend Docker image."
        echo "    all          {Action} all Docker images."
        echo ""
        echo "OPTIONS:"
        echo "    b             {Option} Rebuilds the docker image."
        echo "    s             {Option} Saves the docker image to a .tar file."
        echo ""
        exit 0
    ;;
esac

echo "Saving"


