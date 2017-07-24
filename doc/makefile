.PHONY: all clean

all: build/index.pdf

clean:
	@echo "**************************************************"
	@echo "Cleaning up"
	@echo "**************************************************"
	sudo rm -rf build

build/index.pdf: src/index.tex build/
	@echo "**************************************************"
	@echo "Building $*"
	@echo "**************************************************"
	pdflatex --output-directory build src/index.tex
	pdflatex --output-directory build src/index.tex
	pdflatex --output-directory build src/index.tex

build/:
	mkdir -p $@
