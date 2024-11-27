/* Logico_Biblioteca: */

--Create Schema SisBib

CREATE TABLE SisBib.Biblioteca (
    idBiblioteca int PRIMARY KEY Identity,
    nome varchar(50)
);

CREATE TABLE SisBib.Livro (
    codLivro varchar(6) PRIMARY KEY,
    titulo varchar(100),
    idAutor int,
    idArea int
);

CREATE TABLE SisBib.Leitor (
    idLeitor int PRIMARY KEY Identity,
    nome varchar(50),
    estaSuspenso char(1)
);

CREATE TABLE SisBib.Emprestimo (
    idEmprestimo int PRIMARY KEY Identity,
    idLeitor int,
	idExemplar int,
    dataEmprestimo Date,
    devolucaoEfetiva Date,
    devolucaoPrevista Date
);

CREATE TABLE SisBib.Autor (
    idAutor int PRIMARY KEY Identity,
    nome varchar(50)
);

CREATE TABLE SisBib.Area (
    idArea int PRIMARY KEY,
    nome varchar(50)
);

CREATE TABLE SisBib.Exemplar (
    idExemplar int PRIMARY KEY Identity,
    idBiblioteca int,
    codLivro varchar(6),
    numeroExemplar int
);
 
ALTER TABLE SisBib.Livro ADD CONSTRAINT FK_Livro_2
    FOREIGN KEY (idAutor)
    REFERENCES SisBib.Autor (idAutor)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Livro ADD CONSTRAINT FK_Livro_3
    FOREIGN KEY (idArea)
    REFERENCES SisBib.Area (idArea)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Emprestimo ADD CONSTRAINT FK_Emprestimo_1
    FOREIGN KEY (idLeitor)
    REFERENCES SisBib.Leitor (idLeitor)
    ON DELETE SET NULL;
 
ALTER TABLE SisBib.Emprestimo ADD CONSTRAINT FK_Emprestimo_3
    FOREIGN KEY (idExemplar)
    REFERENCES SisBib.Exemplar (idExemplar);
 
ALTER TABLE SisBib.Exemplar ADD CONSTRAINT FK_Exemplar_1
    FOREIGN KEY (idBiblioteca)
    REFERENCES SisBib.Biblioteca (idBiblioteca)
    ON DELETE CASCADE;
 
ALTER TABLE SisBib.Exemplar ADD CONSTRAINT FK_Exemplar_2
    FOREIGN KEY (codLivro)
    REFERENCES SisBib.Livro (codLivro)
    ON DELETE SET NULL;

-----------------------------------------------
insert into SisBib.Autor (Nome) 
VALUES

('João dos Santos'),
('Maria da Silva'),
('Felipe Alves dos santos')

select * from SisBib.Autor

insert into SisBib.Area (idArea, Nome) 
VALUES
(1, 'Matemática'),
(2, 'Física'),
(3, 'Biologia'),
(4, 'Literatura'),
(5, 'História')

select * from SisBib.Area

insert into SisBib.Livro (codLivro, titulo, idAutor, idArea, ISBN)
VALUES
('XWQ07P', 'Principios da matemática', 1, 1, 'AHBC123414GVC'),
('KVQ84P', 'Física II', 1, 2, 'AHBC948763DVC'),
('HGQ70P', 'Biologia Elementar', 3, 3, 'AHBJ834014GVC'),
('SHQ31P', 'A casa sem paredes', 2, 4, 'AZKJ080414GVC'),
('DIQ43P', 'A grande guerra', 2, 5, 'DLAX827349SJN')

select * from SisBib.Livro

alter table SisBib.Livro
add ISBN varchar(13)

delete from SisBib.Livro

insert into SisBib.Biblioteca (nome)
VALUES
('Dom Pedro II'),
('Maria Antonieta'),
('Carlos Alves'),
('Biblioteca de Alexandria')

select * from SisBib.Exemplar

insert into SisBib.Exemplar (idBiblioteca, codLivro, numeroExemplar)
VALUES
(1, 'XWQ07P', 1),
(1, 'XWQ07P', 2),
(1, 'XWQ07P', 3),
(1, 'XWQ07P', 4),
(2, 'XWQ07P', 5),
(2, 'XWQ07P', 6),
(2, 'XWQ07P', 7),
(3, 'XWQ07P', 8),
(3, 'XWQ07P', 9),
(1, 'KVQ84P', 1),
(2, 'KVQ84P', 2),
(2, 'KVQ84P', 3),
(3, 'KVQ84P', 4),
(4, 'KVQ84P', 5),
(4, 'KVQ84P', 6),
(1, 'HGQ70P', 1),
(2, 'HGQ70P', 2),
(2, 'HGQ70P', 3),
(3, 'HGQ70P', 4),
(4, 'HGQ70P', 5),
(4, 'SHQ31P', 1),
(4, 'SHQ31P', 2),
(1, 'DIQ43P', 1),
(1, 'DIQ43P', 2),
(3, 'DIQ43P', 3),
(3, 'DIQ43P', 4),
(4, 'DIQ43P', 5)



------------------------------------------------------------
--daqui para baixo não foi rodado

insert into SisBib.Leitor (nome, estaSuspenso)
VALUES
('Marcos Aurélio de Souza', 'N'),
('Vinicius da Silva', 'N'),
('Alberto dos Santos', 'N')

select * from SisBib.Emprestimo

-- Ainda não devolveu
insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
VALUES
(1, 1, '2024-11-03', '2024-11-12')  

-- Já devolveu
insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(2, 10, '2024-11-03', '2024-11-12', '2024-11-12')  


-- Devolveu atrasado
insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(2, 10, '2024-11-03', '2024-11-24', '2024-11-12') 

--Teste trigger
insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(2, 10, '2024-11-03', '2024-11-24', '2024-11-12') --Atrasado

insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(1, 1, '2024-11-03', '2024-11-24', '2024-11-12')  --Atrasado

insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(3, 9, '2024-11-03', '2024-12-24', '2024-12-12')  --Atrasado

insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(3, 11, '2024-11-03', '2024-11-24', '2024-11-25') --Adiantado

update SisBib.Emprestimo
set devolucaoPrevista = '2024-11-24'
where idLeitor = 3 and idExemplar = 9

insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoEfetiva, devolucaoPrevista)
VALUES
(2, 7, '2024-11-03', '2024-11-24', '2024-11-24') -- No dia 

insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
VALUES
(3, 7, '2024-11-03', '2024-11-12')  --Não devolveu

delete from SisBib.Emprestimo 
-------------------------------------------------------------
select * from SisBib.Emprestimo

alter view SisBib.AtrasoEMultas
as
select idExemplar as Codigo, 5*DATEDIFF(dd,devolucaoPrevista , devolucaoEfetiva) as multa
from SisBib.Emprestimo
where not DATEDIFF(dd,devolucaoPrevista , devolucaoEfetiva) <= 0

select * from SisBib.AtrasoEMultas

select * from SisBib.Emprestimo

drop trigger SisBib.ImpedeReemprestarLivro_tg
--Essa bomba não quer atualizar

create trigger ImpedeReemprestarExemplar_tg
on SisBib.Emprestimo
instead of insert

as
declare @idExemplar int
declare @idLeitor int
declare @dataEmprestimo date
declare @devolucaoPrevista date

select @idExemplar = idExemplar from inserted
select @idLeitor = idLeitor from inserted
select @dataEmprestimo = dataEmprestimo from inserted
select @devolucaoPrevista = devolucaoPrevista from inserted
print(@idLeitor)

if exists (select * from SisBib.Emprestimo where devolucaoEfetiva is null and idExemplar = @idExemplar)
begin
print('Esse exemplar foi emprestado e ainda não foi devolvido')
return
end
else 
begin
insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
VALUES
(@idLeitor, @idExemplar, @dataEmprestimo, @devolucaoPrevista)
end

sp_help 'SisBib.Emprestimo'



drop proc suspendeLeitor_sp
create proc suspendeLeitor_sp
@idLeitor int
as
update SisBib.Leitor
set estaSuspenso = 'S'
where idLeitor = @idLeitor
	  

suspendeLeitor_sp 2

select * from SisBib.Leitor

