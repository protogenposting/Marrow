using System.Diagnostics;

Process process = new Process();

process.StartInfo.FileName = "java";

process.StartInfo.Arguments = "-jar "+System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location)+"\\Marrow.jar";

process.Start();