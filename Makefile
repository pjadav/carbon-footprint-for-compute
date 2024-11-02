.PHONY: fmt test coverage buildImage pushImage localDevUp localDevDown

fmt:
	sbt scalafmtAll
test:
	sbt clean compile test

coverage:
	sbt clean coverage test
	sbt coverageReport

assembly:
	sbt -J-Xmx2g clean assembly

buildImage:
	docker build . -t carbonapi

runAPIServer:
	docker run -p 8080:8080 -e API_KEY=${API_KEY} carbonapi:latest