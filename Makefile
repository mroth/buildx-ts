IMAGE := buildx-ts

build-driver-default:
	docker buildx use default
	docker buildx build -t $(IMAGE):default --progress=auto .

build-driver-container:
	docker buildx create --name cbuilder --driver docker-container --use
	docker buildx build -t $(IMAGE):container --progress=auto --load .	
	docker buildx rm cbuilder

run-default: | build-driver-default
	docker run --rm -it $(IMAGE):default sh script-runtime.sh

run-container: | build-driver-container
	docker run --rm -it $(IMAGE):container sh script-runtime.sh
