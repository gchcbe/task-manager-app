#!/bin/bash

# ══════════════════════════════════════════════════════════
#  Task Manager App — Build & Deploy Script
#  Usage: ./build.sh [OPTIONS]
#
#  Options:
#    --full          Full build: compile + package + docker (default)
#    --fast          Skip tests and npm install (faster rebuild)
#    --backend-only  Build and redeploy backend only
#    --frontend-only Build and redeploy frontend only
#    --no-cache      Force Docker to rebuild images from scratch
#    --start-only    Just start containers (no build)
#    --stop          Stop and remove all containers
#    --with-selenium Run Selenium tests after deployment
#    --help          Show this help message
# ══════════════════════════════════════════════════════════

set -e

# ── Colors ──
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# ── Defaults ──
BUILD_BACKEND=true
BUILD_FRONTEND=true
BUILD_DOCKER=true
SKIP_TESTS=false
SKIP_NPM_INSTALL=false
NO_CACHE=false
START_ONLY=false
STOP_ONLY=false
RUN_SELENIUM=false

# ── Root directory (always run from project root) ──
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# ── Helper functions ──
print_header() {
  echo ""
  echo -e "${BLUE}══════════════════════════════════════════════════${NC}"
  echo -e "${BLUE}  Task Manager App — Build & Deploy${NC}"
  echo -e "${BLUE}══════════════════════════════════════════════════${NC}"
  echo -e "${CYAN}  Mode: $1${NC}"
  echo -e "${BLUE}══════════════════════════════════════════════════${NC}"
}

print_step() {
  echo ""
  echo -e "${YELLOW}▶ $1${NC}"
}

print_success() {
  echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
  echo -e "${RED}❌ $1${NC}"
}

print_help() {
  echo ""
  echo -e "${CYAN}Usage: ./build.sh [OPTION]${NC}"
  echo ""
  echo "Options:"
  echo "  --full           Full build: compile + package + docker (default)"
  echo "  --fast           Skip tests and npm install (faster rebuild)"
  echo "  --backend-only   Build and redeploy backend only"
  echo "  --frontend-only  Build and redeploy frontend only"
  echo "  --no-cache       Force Docker to rebuild images from scratch"
  echo "  --start-only     Just start containers without building"
  echo "  --stop           Stop and remove all running containers"
  echo "  --with-selenium  Run Selenium tests after deployment"
  echo "  --help           Show this help message"
  echo ""
  echo "Examples:"
  echo "  ./build.sh                  # Full build and deploy"
  echo "  ./build.sh --fast           # Quick rebuild skipping tests"
  echo "  ./build.sh --backend-only   # Rebuild backend only"
  echo "  ./build.sh --no-cache       # Force fresh Docker build"
  echo "  ./build.sh --stop           # Stop everything"
  echo "  ./build.sh --with-selenium  # Full build + run Selenium tests"
  echo ""
}

# ── Parse arguments ──
if [ $# -eq 0 ]; then
  MODE="Full Build"
else
  case "$1" in
    --full)
      MODE="Full Build"
      ;;
    --fast)
      MODE="Fast Build (skip tests + npm install)"
      SKIP_TESTS=true
      SKIP_NPM_INSTALL=true
      ;;
    --backend-only)
      MODE="Backend Only"
      BUILD_FRONTEND=false
      ;;
    --frontend-only)
      MODE="Frontend Only"
      BUILD_BACKEND=false
      ;;
    --no-cache)
      MODE="Full Build (no Docker cache)"
      NO_CACHE=true
      ;;
    --start-only)
      MODE="Start Containers Only"
      BUILD_BACKEND=false
      BUILD_FRONTEND=false
      BUILD_DOCKER=false
      START_ONLY=true
      ;;
    --stop)
      MODE="Stop All Containers"
      STOP_ONLY=true
      ;;
    --with-selenium)
      MODE="Full Build + Selenium Tests"
      RUN_SELENIUM=true
      ;;
    --help)
      print_help
      exit 0
      ;;
    *)
      print_error "Unknown option: $1"
      print_help
      exit 1
      ;;
  esac
fi

print_header "$MODE"

# ── Stop containers ──
print_step "Stopping any running containers..."
if [ "$(docker-compose ps -q 2>/dev/null)" ]; then
  docker-compose down
  print_success "Containers stopped and removed"
else
  echo "  No running containers found — skipping"
fi

# ── Exit if stop only ──
if [ "$STOP_ONLY" = true ]; then
  print_success "All containers stopped. Done!"
  exit 0
fi

# ── Backend Build ──
if [ "$BUILD_BACKEND" = true ]; then
  print_step "[Backend] Compiling and packaging Spring Boot..."
  cd backend/taskmanager

  if [ "$SKIP_TESTS" = true ]; then
    mvn clean package -DskipTests
  else
    mvn clean package
  fi

  print_success "Backend packaged → target/taskmanager-*.jar"
  cd "$SCRIPT_DIR"
fi

# ── Frontend Build ──
if [ "$BUILD_FRONTEND" = true ]; then
  print_step "[Frontend] Building React app..."
  cd frontend/taskmanager-ui

  if [ "$SKIP_NPM_INSTALL" = false ]; then
    npm install
  fi

  npm run build
  print_success "Frontend built → build/"
  cd "$SCRIPT_DIR"
fi

# ── Docker Build ──
if [ "$BUILD_DOCKER" = true ]; then
  print_step "[Docker] Building images..."

  if [ "$NO_CACHE" = true ]; then
    docker-compose build --no-cache
  else
    docker-compose build
  fi

  print_success "Docker images built"
fi

# ── Start containers ──
print_step "Starting all containers..."
docker-compose up -d

# ── Wait and health check ──
print_step "Waiting for services to be healthy..."
sleep 5

echo ""
echo -e "${CYAN}Container Status:${NC}"
docker-compose ps

echo ""
echo -e "${GREEN}══════════════════════════════════════════════════${NC}"
echo -e "${GREEN}  ✅ Build & Deploy Complete!${NC}"
echo -e "${GREEN}══════════════════════════════════════════════════${NC}"
echo -e "  🌐 Frontend : http://localhost:3000"
echo -e "  🔧 Backend  : http://localhost:8080/api/tasks"
echo -e "  🗄️  Database : localhost:5432"
echo -e "${GREEN}══════════════════════════════════════════════════${NC}"
echo ""

# ── Selenium Tests (optional) ──
if [ "$RUN_SELENIUM" = true ]; then
  print_step "Waiting for app to be ready before running Selenium tests..."
  sleep 15

  print_step "Running Selenium tests..."
  cd backend/taskmanager
  if mvn test -Dtest=TaskManagerSeleniumTest 2>&1 | tee /tmp/selenium-results.log | grep -E "(Tests run|BUILD)"; then
    print_success "All Selenium tests passed"
  else
    print_error "Selenium tests failed — see output above"
    cd "$SCRIPT_DIR"
    exit 1
  fi
  cd "$SCRIPT_DIR"
fi