SHELL := /bin/bash

all:
	pandoc -V theme:Warsaw --variable fontsize=8pt -t beamer -s presentation.md -o resources/presentations/presentation.pdf
	xdg-open resources/presentations/presentation.pdf

pdf:
	pandoc -s presentation.md -o presentation.pdf
	xdg-open resources/presentations/presentation.pdf

clean:
	rm presentation.pdf resources/presentations/presentation.pdf
	rm -fr resources/example-app{0..2}/{bin,gen}

app:
	for i in {0..2}; do \
		cd resources/example-app$$i;\
		cp local.properties-example local.properties;\
		ant debug;\
		cd ../..;\
	done

