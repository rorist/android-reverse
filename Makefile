SHELL := /bin/bash

all:
	pandoc -V theme:Warsaw --variable fontsize=8pt -t beamer -s presentation.md -o presentation.pdf
	xdg-open presentation.pdf

pdf:
	pandoc -s presentation.md -o presentation.pdf
	xdg-open presentation.pdf

clean:
	rm presentation.pdf
	rm -fr resources/example-app{0..2}/{bin,gen}
