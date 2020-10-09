The modified model is based on the original MelGAN model, see the report for more details.

Based on our experiments, we modify the model by changing the pooling and padding fuction, 
decreasing the number of discriminators to 1 and adding one extra residual block to the 
generator architecture.

Our results show that the modified model outperform the original model and increase the PESQ
score by 0.5.

To run the codes, you need to:
1) Download the LJ dataset: https://keithito.com/LJ-Speech-Dataset/, and put the .wav files 
into wavs/ subfolder;
2) Check the required libraries:
	python(>=3.7)
	torch(>=1.2)
	librosa
	pyyaml
	scipy
	argparse
3) Train the model:
    a. train-test split
	ls wavs/*.wav | tail -n+10 > train_files.txt
	ls wavs/*.wav | head -n10 > test_files.tx
    b. set PYTHONPATH and use first GPU
	. source set_env.sh 0
    c. train MelGAN model
	python scripts/train.py --save_path data/logs --data_path <root_data_folder>