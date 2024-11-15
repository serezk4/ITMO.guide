.PHONY: run down build up clean reset restart logs ps

# Run the application by building and starting the containers
run: down build up

# Stop and remove containers, networks, and volumes
down:
	@echo "Stopping and removing containers..."
	docker compose down

# Build or rebuild services
build:
	@echo "Building services..."
	docker compose build

# Start containers in detached mode
up:
	@echo "Starting containers..."
	docker compose up -d

# Reset by stopping and removing containers, networks, and volumes
reset:
	@echo "Resetting by removing containers and volumes..."
	docker compose down -v

# Restart the containers
restart: down up

# Show logs for the containers
logs:
	@echo "Showing logs..."
	docker compose logs -f

# Show running containers
ps:
	@echo "Listing running containers..."
	docker compose ps