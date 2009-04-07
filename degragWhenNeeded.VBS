Set objShell = CreateObject("Wscript.Shell")
Set objWshScriptExec = objShell.Exec("defrag.exe c: -a")
strOutput=objWshScriptExec.StdOut.ReadAll
strSearch = "You do not need to defragment this volume"
If InStr(1, strOutput, strSearch) = 0 Then
objShell.Run ("defrag.exe c:")
End If
