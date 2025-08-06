# RumbleDB

With RumbleDB, you can query with ease a lot of different nested, heterogeneous data formats like JSON, CSV, Parquet, Avro, LibSVM, text, etc.

RumbleDB exposes a query language rather than a DataFrame API, for more flexibility, more productivity but also because a lot of data simply will not fit in DataFrames.

You can query it in place from any local file systems or data lakes (Azure blob storage, Amazon S3, HDFS, etc).

You can prepare, clean up, validate your data and put it right into your machine learning pipelines with RumbleDB ML.

Getting started: you will find a Jupyter notebook that introduces the JSONiq language on top of RumbleDB [here](https://colab.research.google.com/github/RumbleDB/rumble/blob/master/RumbleSandbox.ipynb). You can also run it locally if you prefer.

The documentation also contains an introduction specific to RumbleDB and how you can read input datasets, but we have not converted it to Jupyter notebooks yet (this will follow).

[The documentation of the latest official release is available here.](http://rumble.readthedocs.io/en/latest/)

RumbleDB is an effort involving many researchers and ETH Zurich students: code and support by Stefan Irimescu, Ghislain Fourny, Gustavo Alonso, Renato Marroquin, Rodrigo Bruno, Falko Noé, Ioana Stefan, Andrea Rinaldi, Stevan Mihajlovic, Mario Arduini, Can Berker Çıkış, Elwin Stephan, David Dao, Zirun Wang, Ingo Müller, Dan-Ovidiu Graur, Thomas Zhou, Olivier Goerens, Alexandru Meterez, Remo Röthlisberger, Dominik Bruggisser, David Loughlin, David Buzatu, Marco Schöb, Maciej Byczko, Dwij Dixit, Matteo Agnoletto.
