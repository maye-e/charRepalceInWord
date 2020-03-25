# charRepalceInWord sprintboot版
yml文件中获取map时，若 key 为中文，则获取出错，若key或value包含特殊字符，如：# * ? & % 等，则会被过滤。因此，在yml中用两个 list 来配置，代码中将其一一对应
