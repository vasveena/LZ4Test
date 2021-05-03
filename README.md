# LZ4Test

Install git and mvn - https://gist.github.com/sebsto/19b99f1fa1f32cae5d00
sudo yum install git

git clone https://github.com/vasveena/LZ4Test.git

cd LZ4Test

mvn clean package

java -Xms4g -Xmx4g -jar target/LZ4Test-1.0-SNAPSHOT-withdep.jar
