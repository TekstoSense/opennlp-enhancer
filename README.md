# Opennlp Enhancer

opennlp-enhancer is an extended framework of NLP(Natural Language Processing) to enhance its performance. It extensively supports **Named Entity** extraction and **Memory Optimization** by extending [opennlp](https://opennlp.apache.org/) framweork.

## Features

1. **Memory Optimization** 
   
   opennlp-enhancer reduces the size of models loaded into memory by 1/4th of it size. It also uses L-BFGS algorithm to train models and hence it takes less time to converge and it also reduces the size of models hence opennlp-enhancer optimizes the memory by doublefold. 

2. **Extensive Named Entity support**

   It supports extensive set of free to use openNLP based models. All these models are trained using both "Maxent" and "L-BFGS" based algorithms. We have deployed all L-BFGS based models for your use in our [**demo**](http://apis.tekstosense.com) site. It takes less memory space(reduced by 1/4) than "Maxent" based model.  
   
   * Airline
   * Album
   * Anatomical Structure
   * Book
   * Broadcast
   * ChemicalCompound
   * Company
   * Disease
   * Drug
   * EducationalInstitution
   * Film
   * Magazine
   * Newspaper
   * Non-ProfitOrganisation
   * Organisation
   * Person
   * Place
   * Plant
   * Software
   * Species
   * Sport

## Dependency

`opennlp 1.6.0`

## License

`Apache 2.0`


